package ec.edu.espe.banquito.requirements.service;

import ec.edu.espe.banquito.requirements.controller.DTO.*;
import ec.edu.espe.banquito.requirements.model.InterestAccrue;
import ec.edu.espe.banquito.requirements.model.Loan;
import ec.edu.espe.banquito.requirements.model.LoanTransaction;
import ec.edu.espe.banquito.requirements.model.Payment;
import ec.edu.espe.banquito.requirements.repository.LoanRepository;
import ec.edu.espe.banquito.requirements.repository.LoanTransactionRepository;
import ec.edu.espe.banquito.requirements.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final LoanTransactionRepository loanTransactionRepository;

    public LoanService(LoanRepository loanRepository,
                       PaymentRepository paymentRepository,
                       LoanTransactionRepository loanTransactionRepository) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
        this.loanTransactionRepository = loanTransactionRepository;
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
    public LoanRS create(LoanRQ loanRQ, PaymentRQ paymentRQ, LoanTransactionRQ loanTransactionRQ) {
        Loan newLoan = this.transformLoanRQ(loanRQ);

        /* Verificar existencia de préstamos para cliente y grupo de compañia */
        Loan existLoanCustomer = this.loanRepository.findByCustomerIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(
                loanRQ.getCustomerId(), loanRQ.getLoanProductId(), loanRQ.getLoanHolderType(), loanRQ.getLoanHolderCode()
        );
        Loan existLoanGroupCompany = this.loanRepository.findByGroupCompanyIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(
                loanRQ.getGroupCompanyId(), loanRQ.getLoanProductId(), loanRQ.getLoanHolderType(), loanRQ.getLoanHolderCode()
        );

        /* Verificación si es Customer o Group Company */
        if ((existLoanCustomer == null && existLoanGroupCompany == null) ||
                (existLoanCustomer != null && existLoanGroupCompany != null)) {
            throw new RuntimeException("Debe ser cliente o grupo de compañía, pero no ambos.");
        }

        /* Detalles de prestamo */
        newLoan.setUniqueKey(UUID.randomUUID().toString());
        newLoan.setStatus("APR");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, newLoan.getTerm());
        Date newDate = calendar.getTime();
        newLoan.setApprovalDate(new Date());
        newLoan.setDueDate(newDate);

        /* Guardar Loan */
        Loan resultLoan = loanRepository.save(newLoan);

        /* Crear y guardar LoanTransaction */
        LoanTransaction newLoanTransaction = transformLoanTransactionRQ(loanTransactionRQ);
        LoanTransaction existingLoanTransaction = loanTransactionRepository.findByTypeAndStatusAndAmountAndCreationDate(
                newLoanTransaction.getType(), newLoanTransaction.getStatus(), newLoanTransaction.getAmount(),
                newLoanTransaction.getCreationDate()
        );

        if (existingLoanTransaction == null) {
            LoanTransaction resultLoanTransaction = loanTransactionRepository.save(newLoanTransaction);

            /* Crear y guardar Payment */
            Payment newPayment = transformPaymentRQ(paymentRQ);
            Payment existingPayment = paymentRepository.findByLoanIdAndLoanTransactionId(newPayment.getLoanId(),
                    newPayment.getLoanTransactionId());
            if (existingPayment == null) {
                Payment resultPayment = paymentRepository.save(newPayment);
            } else {
                throw new RuntimeException("Ya existe el pago.");
            }
        } else {
            throw new RuntimeException("Ya existe la transacción de préstamo.");
        }
        return this.transformLoan(resultLoan);
    }

    //Response Payment
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
                .monthlyFee(loan.getMonthlyFee())
                .lastPaymentDueDate(loan.getLastPaymentDueDate())
                .daysLate(loan.getDaysLate())
                .interestRate(loan.getInterestRate())
                .build();
    }

    //Request Loan
    private Loan transformLoanRQ(LoanRQ rq) {
        return Loan
                .builder()
                .groupCompanyId(rq.getGroupCompanyId())
                .customerId(rq.getCustomerId())
                .guarantorId(rq.getGuarantorId())
                .branchId(rq.getBranchId())
                .loanProductId(rq.getLoanProductId())
                .assetId(rq.getAssetId())
                .loanHolderType(rq.getLoanHolderType())
                .loanHolderCode(rq.getLoanHolderCode())
                .name(rq.getName())
                .amount(rq.getAmount())
                .term(rq.getTerm())
                .gracePeriod(rq.getGracePeriod())
                .gracePeriodType(rq.getGracePeriodType())
                .monthlyFee(rq.getMonthlyFee())
                .interestRate(rq.getInterestRate())
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
                .loanId(paymentRQ.getLoanId())
                .loanTransactionId(paymentRQ.getLoanTransactionId())
                .type(paymentRQ.getType())
                .reference(paymentRQ.getReference())
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
                .type(loanTransactionRQ.getType())
                .amount(loanTransactionRQ.getAmount())
                .applyTax(loanTransactionRQ.getApplyTax())
                .notes(loanTransactionRQ.getNotes())
                .uniqueKey(UUID.randomUUID().toString())
                .creationDate(new Date())
                .bookingDate(new Date())
                .valueDate(new Date())
                .build();
    }
}
