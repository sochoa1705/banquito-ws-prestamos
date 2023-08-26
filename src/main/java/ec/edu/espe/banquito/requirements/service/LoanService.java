package ec.edu.espe.banquito.requirements.service;

import ec.edu.espe.banquito.requirements.controller.DTO.*;
import ec.edu.espe.banquito.requirements.model.*;
import ec.edu.espe.banquito.requirements.repository.*;
import ec.edu.espe.banquito.requirements.service.ExternalRestServices.AccountTransactionRestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final LoanTransactionRepository loanTransactionRepository;
    private final AssetRepository assetRepository;
    private final GuarantorRepository guarantorRepository;
    private final AccountTransactionRestService accountTransactionRestService;


    public LoanRS getLoanByCustomerId(String customerId){
        Loan optionalLoan = loanRepository.findByCustomerId(customerId);
        return this.transformLoan(optionalLoan);
    }

    public LoanRS getLoanByGroupCompanyId(String groupCompanyId){
        Loan optionalLoan = loanRepository.findByGroupCompanyId(groupCompanyId);
        return this.transformLoan(optionalLoan);
    }

    @Transactional
    public LoanRS create(LoanProcessRQ loanProcessRQ) {

        /* Validación campos GroupCompanyId o CustomerId */
        if((loanProcessRQ.getGroupCompanyId() == null && loanProcessRQ.getCustomerId() == null)
                || (loanProcessRQ.getGroupCompanyId() != null && loanProcessRQ.getCustomerId() != null)){
            throw new RuntimeException("Los dos ids vacios o llenos, debe solo uno estar lleno");
        }

        /* Crear objeto prestamo */
        Loan newLoan = Loan.builder()
                .groupCompanyId(loanProcessRQ.getGroupCompanyId())
                .customerId(loanProcessRQ.getCustomerId())
                .branchId(loanProcessRQ.getBranchId())
                .loanProductId(loanProcessRQ.getLoanProductId())
                .loanHolderType(loanProcessRQ.getLoanHolderType())
                .loanHolderCode(loanProcessRQ.getLoanHolderCode())
                .name(loanProcessRQ.getLoanName())
                .amount(loanProcessRQ.getLoanAmount())
                .term(loanProcessRQ.getLoanTerm())
                .gracePeriod(loanProcessRQ.getLoanGracePeriod())
                .gracePeriodType(loanProcessRQ.getLoanGracePeriodType())
                .interestRate(loanProcessRQ.getLoanInterestRate())
                .quote(loanProcessRQ.getLoanQuote())
                .build();

        /* Verificar existencia de préstamos para cliente y grupo de compañia */
        Loan existLoanCustomer = this.loanRepository.findByCustomerIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(
                newLoan.getCustomerId(), newLoan.getLoanProductId(), newLoan.getLoanHolderType(), newLoan.getLoanHolderCode()
        );
        Loan existLoanGroupCompany = this.loanRepository.findByGroupCompanyIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(
                newLoan.getGroupCompanyId(), newLoan.getLoanProductId(), newLoan.getLoanHolderType(), newLoan.getLoanHolderCode()
        );

        if (existLoanCustomer != null || existLoanGroupCompany != null) {
            throw new RuntimeException("Ya existe prestamo");
        }

        /* Creación del guarantor */
        Guarantor createdGuarantor = this.createGuarantor(loanProcessRQ);

        /* Creación del activo */
        Asset createdAsset = this.createAsset(loanProcessRQ);

        /* Detalles del prestamo */
        newLoan.setGuarantorId(createdGuarantor.getId());
        newLoan.setAssetId(createdAsset.getId());
        newLoan.setUniqueKey(UUID.randomUUID().toString());
        newLoan.setStatus("APR");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, newLoan.getTerm()+newLoan.getGracePeriod());
        Date newDate = calendar.getTime();
        newLoan.setApprovalDate(new Date());
        newLoan.setDueDate(newDate);

        /* Guardo loan */
        Loan createdLoan = loanRepository.save(newLoan);
        log.info("Punto C: Después de la creación del prestamo...");

        /* Creación objeto loanTransaction - desembolso */
        LoanTransaction newLoanTransaction = LoanTransaction.builder()
                .uniqueKey(UUID.randomUUID().toString())
                .type("DIS") //Fondos del prestamo entregado al cliente
                .creationDate(new Date())
                .bookingDate(new Date())
                .valueDate(new Date())
                .status("COM")
                .amount(createdLoan.getAmount())
                .applyTax(false)
                .notes("Fondos prestamo entregado al cliente")
                .build();

        /* valido si ya se realizo la transacción */
        LoanTransaction firstTransaction = loanTransactionRepository.findByUniqueKey(newLoanTransaction.getUniqueKey());
        log.info("Punto D: Después de la asignacion de campos en loan transaction...");
        if(firstTransaction != null){
            throw new RuntimeException("Ya existe loan transaction");
        }

        /* Guardar loan-transaction */
        LoanTransaction createdLoanTransaction = loanTransactionRepository.save(newLoanTransaction);

        /* Crear objeto payment */
        log.info("Punto E: Después de la creación de la transacción de loan...");
        Payment createdPayment = null;
        if(loanProcessRQ.getBankCode() != null && loanProcessRQ.getAccount() != null){
            log.info("Punto F: Ingreso al if de payment...");
            Payment newPayment = Payment.builder()
                    .loanId(createdLoan.getId())
                    .loanTransactionId(createdLoanTransaction.getId())
                    .type(createdLoanTransaction.getType())
                    .reference("Banco: "+loanProcessRQ.getBankCode()+" cuenta destino: "+loanProcessRQ.getAccount())
                    .status("COM")
                    .creditorAccount(loanProcessRQ.getAccount())
                    .creditorBankCode(loanProcessRQ.getBankCode())
                    .debtorAccount(null)
                    .debtorBankCode(null)
                    .dueDate(new Date())
                    .build();
            log.info("Punto G: Después de la asignación del newPayment...");
            /* Guardar el payment */
            createdPayment = paymentRepository.save(newPayment);
        }else{
            throw new RuntimeException("Error payment");
        }

        //Desembolso del prestamo - Pendiente asignación del uuid al createdPayment
        accountTransactionRestService.sendAccountTransactionCreationRequest(createdPayment.getCreditorAccount(),
                createdPayment.getDebtorAccount(), "CRED", null, createdLoan.getAmount().floatValue(),"Desembolso prestamo");

        /* Suponiendo que paso sin problemas */

        //Generación de primer pago del cliente
        LoanTransaction fisrtPaymenLoantransaction = LoanTransaction.builder()
                .uniqueKey(UUID.randomUUID().toString())
                .type("REP") //Fondos del prestamo entregado al cliente
                .creationDate(new Date())
                .bookingDate(new Date())
                .valueDate(new Date())
                .status("PEN")
                .amount(createdLoan.getQuote())
                .applyTax(false)
                .notes("Primer pago del cliente")
                .parentLoanTrxKey(createdLoanTransaction.getUniqueKey())
                .build();

        LoanTransaction saveFisrtPaymenLoantransaction = loanTransactionRepository.save(fisrtPaymenLoantransaction);

        /* Calculo de fecha primer pago */
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, newLoan.getGracePeriod()+1);
        /* Creación objeto primer pago */
        Payment firstPayment = Payment.builder()
                .loanId(createdLoan.getId())
                .loanTransactionId(saveFisrtPaymenLoantransaction.getId())
                .type("PRI")
                .reference("Pago primer mes al banco")
                .status("PEN")
                .creditorAccount(null)
                .creditorBankCode(null)
                .debtorAccount(loanProcessRQ.getAccount())
                .debtorBankCode(loanProcessRQ.getBankCode())
                .dueDate(calendar1.getTime())
                .build();

        return this.transformLoan(createdLoan);
    }

    //Creación del garante
    private Guarantor createGuarantor(LoanProcessRQ loanProcessRQ){
        if (loanProcessRQ.getGuarantorCode() != null &&
                loanProcessRQ.getGuarantorType() != null &&
                loanProcessRQ.getGuarantorName() != null) {
            return guarantorRepository.save(
                    Guarantor.builder()
                            .code(loanProcessRQ.getGuarantorCode())
                            .type(loanProcessRQ.getGuarantorType())
                            .name(loanProcessRQ.getGuarantorName())
                            .status("ACT")
                            .build());
        } else {
            throw new RuntimeException("Error al crear garante");
        }
    }

    //Creación del asset
    private Asset createAsset(LoanProcessRQ loanProcessRQ){
        if(loanProcessRQ.getAssetAmount() != null
                && loanProcessRQ.getAssetType() != null
                && loanProcessRQ.getAssetName() != null
                && loanProcessRQ.getAssetCurrency() != null){
            return assetRepository.save(
                    Asset.builder()
                            .amount(loanProcessRQ.getAssetAmount())
                            .type(loanProcessRQ.getAssetType())
                            .name(loanProcessRQ.getAssetName())
                            .currency(loanProcessRQ.getAssetCurrency())
                            .status("ACT")
                            .build());
        }else{
            throw new RuntimeException("Error al crear asset");
        }
    }
    //Response Loan
    private LoanRS transformLoan(Loan loan) {
        return LoanRS
                .builder()
                .groupCompanyId(loan.getGroupCompanyId())
                .customerId(loan.getCustomerId())
                .guarantorId(loan.getGuarantorId())
                .branchId(loan.getBranchId())
                .loanProductId(loan.getLoanProductId())
                .assetId(loan.getAssetId())
                .uniqueKey(loan.getUniqueKey())
                .loanHolderType(loan.getLoanHolderType())
                .loanHolderCode(loan.getLoanHolderCode())
                .name(loan.getName())
                .amount(loan.getAmount())
                .term(loan.getTerm())
                .gracePeriod(loan.getGracePeriod())
                .gracePeriodType(loan.getGracePeriodType())
                .status(loan.getStatus())
                .approvalDate(loan.getApprovalDate())
                .dueDate(loan.getDueDate())
                .lastPaymentDueDate(loan.getLastPaymentDueDate())
                .daysLate(loan.getDaysLate())
                .interestRate(loan.getInterestRate())
                .quote(loan.getQuote())
                .build();
    }

    //Request Loan
    private Loan transformLoanRQ(LoanRQ rq) {
        return Loan
                .builder()
                .groupCompanyId(rq.getGroupCompanyId())
                .customerId(rq.getCustomerId())
                .branchId(rq.getBranchId())
                .loanProductId(rq.getLoanProductId())
                .loanHolderType(rq.getLoanHolderType())
                .loanHolderCode(rq.getLoanHolderCode())
                .name(rq.getName())
                .amount(rq.getAmount())
                .term(rq.getTerm())
                .gracePeriod(rq.getGracePeriod())
                .gracePeriodType(rq.getGracePeriodType())
                .interestRate(rq.getInterestRate())
                .quote(rq.getQuote())
                .build();
    }

    //Response Payment
    private PaymentRS transformToPaymentRS(Payment payment){
        return PaymentRS.builder()
                .id(payment.getId())
                .loanId(payment.getLoanId())
                .loanTransactionId(payment.getLoanTransactionId())
                .type(payment.getType())
                .reference(payment.getReference())
                .status(payment.getStatus())
                .creditorBankCode(payment.getCreditorBankCode())
                .creditorAccount(payment.getCreditorAccount())
                .debtorAccount(payment.getDebtorAccount())
                .debtorBankCode(payment.getDebtorBankCode())
                .build();
    }

    //Request payment
    private Payment transformPaymentRQ(PaymentRQ paymentRQ){
        return Payment.builder()

                .status("PEN")
                .creditorBankCode("BANQUI001") //Cambiar por un método o cambiar el string
                .creditorAccount(paymentRQ.getCreditorAccount())
                .debtorAccount(paymentRQ.getDebtorAccount())
                .debtorBankCode("BANQUI001") //Cambiar por un método o cambiar el string
                .build();
    }

    //Response LoanTransaction
    private LoanTransactionRS transformtoLoanTransactionRS(LoanTransaction loanTransaction){
        return LoanTransactionRS.builder()
                .id(loanTransaction.getId())
                .uniqueKey(loanTransaction.getUniqueKey())
                .type(loanTransaction.getType())
                .bookingDate(loanTransaction.getBookingDate())
                .valueDate(loanTransaction.getValueDate())
                .status(loanTransaction.getStatus())
                .amount(loanTransaction.getAmount())
                .applyTax(loanTransaction.getApplyTax())
                .parentLoanTrxKey(loanTransaction.getParentLoanTrxKey())
                .notes(loanTransaction.getNotes())
                .build();
    }

    //Request LoanTransaction
    private LoanTransaction transformLoanTransactionRQ(LoanTransactionRQ loanTransactionRQ){
        return LoanTransaction.builder()

                .uniqueKey(UUID.randomUUID().toString())
                .creationDate(new Date())
                .bookingDate(new Date())
                .valueDate(new Date())
                .build();
    }
}
