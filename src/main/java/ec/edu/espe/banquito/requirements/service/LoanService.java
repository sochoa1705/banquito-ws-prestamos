package ec.edu.espe.banquito.requirements.service;

import ec.edu.espe.banquito.requirements.controller.DTO.*;
import ec.edu.espe.banquito.requirements.model.*;
import ec.edu.espe.banquito.requirements.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final LoanTransactionRepository loanTransactionRepository;
    private final AssetRepository assetRepository;
    private final GuarantorRepository guarantorRepository;

    public LoanService(LoanRepository loanRepository, PaymentRepository paymentRepository,
                       LoanTransactionRepository loanTransactionRepository, AssetRepository assetRepository,
                       GuarantorRepository guarantorRepository) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
        this.loanTransactionRepository = loanTransactionRepository;
        this.assetRepository = assetRepository;
        this.guarantorRepository = guarantorRepository;
    }

    public LoanRS getLoanByCustomerId(Integer customerId){
        Loan optionalLoan = loanRepository.findByCustomerId(customerId);
        return this.transformLoan(optionalLoan);
    }

    public LoanRS getLoanByGroupCompanyId(Integer groupCompanyId){
        Loan optionalLoan = loanRepository.findByGroupCompanyId(groupCompanyId);
        return this.transformLoan(optionalLoan);
    }

    @Transactional
    public LoanRS create(LoanProcessRQ loanProcessRQ) {
        log.info("Iniciando método create...");
        /* Creación del guarantor */
        Guarantor createdGuarantor = null;
        if(loanProcessRQ.getGuarantorCode() != null && loanProcessRQ.getGuarantorType() != null
                && loanProcessRQ.getGuarantorName() != null){
            createdGuarantor = guarantorRepository.save(
                            Guarantor.builder()
                            .code(loanProcessRQ.getGuarantorCode())
                            .type(loanProcessRQ.getGuarantorType())
                            .name(loanProcessRQ.getGuarantorName())
                            .status("ACT")
                            .build());
        }else{
            throw new RuntimeException("Error guarantor");
        }
        log.info("Punto A: Después de la creación del guarantor...");
        /* Creación de la garantia */
        Asset createdAsset = null;
        if(loanProcessRQ.getAssetAmount() != null && loanProcessRQ.getAssetType() != null
                && loanProcessRQ.getAssetName() != null && loanProcessRQ.getAssetCurrency() != null){
            createdAsset = assetRepository.save(
                            Asset.builder()
                            .amount(loanProcessRQ.getAssetAmount())
                            .type(loanProcessRQ.getAssetType())
                            .name(loanProcessRQ.getAssetName())
                            .currency(loanProcessRQ.getAssetCurrency())
                            .status("ACT")
                            .build());
        }else{
            throw new RuntimeException("Error Assets");
        }
        log.info("Punto B: Después de la creación de la garantía...");
        /* Validación de que solo debe recibir un Group Comany Id ó CustomerId*/
        if((loanProcessRQ.getGroupCompanyId() == null && loanProcessRQ.getCustomerId() == null)
        || (loanProcessRQ.getGroupCompanyId() != null && loanProcessRQ.getCustomerId() != null)){
            throw new RuntimeException();
        }

        /* Crear objeto prestamo */
        Loan newLoan = Loan.builder()
                .groupCompanyId(loanProcessRQ.getGroupCompanyId())
                .customerId(loanProcessRQ.getCustomerId())
                .guarantorId(createdGuarantor.getId())
                .branchId(loanProcessRQ.getBranchId())
                .loanProductId(loanProcessRQ.getLoanProductId())
                .assetId(createdAsset.getId())
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

        /* Verificación si existe para Customer ó Group Company */
        if ((existLoanCustomer != null && existLoanGroupCompany == null) ||
                (existLoanCustomer == null && existLoanGroupCompany != null)) {
            throw new RuntimeException("Ya existe prestamo");
        }

        /* Detalles del prestamo */
        newLoan.setUniqueKey(UUID.randomUUID().toString());
        newLoan.setStatus("APR");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, newLoan.getTerm()+newLoan.getGracePeriod());
        Date newDate = calendar.getTime();
        newLoan.setApprovalDate(new Date());
        newLoan.setDueDate(newDate);

        Loan createdLoan = loanRepository.save(newLoan);
        log.info("Punto C: Después de la creación del prestamo...");
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

        LoanTransaction firstTransaction = loanTransactionRepository.findByUniqueKey(newLoanTransaction.getUniqueKey());
        log.info("Punto D: Después de la asignacion de campos en loan transaction...");
        if(firstTransaction != null){
            throw new RuntimeException("Ya existe");
        }
        LoanTransaction createdLoanTransaction = loanTransactionRepository.save(newLoanTransaction);
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
            System.out.println("Contenido de newPayment: " + newPayment);
            try{
                createdPayment = paymentRepository.save(newPayment);
                log.info("Punto H: Después de la creación del Payment...");
            } catch (Exception e){
                log.error("Error al guardar el nuevo pago: " + e.getMessage(), e);
                throw new RuntimeException("Error payment");
            }
            log.info("Punto I: Después del try-catch del payment...");
        }else{
            throw new RuntimeException("Error payment");
        }
        return this.transformLoan(createdLoan);
    }

    //Response Payment
    private LoanRS transformLoan(Loan loan) {
        return LoanRS
                .builder()
                .id(loan.getId())
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
