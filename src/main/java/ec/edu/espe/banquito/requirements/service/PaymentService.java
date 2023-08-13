package ec.edu.espe.banquito.requirements.service;

import ec.edu.espe.banquito.requirements.controller.DTO.PaymentRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.PaymentRS;
import ec.edu.espe.banquito.requirements.model.Payment;
import ec.edu.espe.banquito.requirements.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentRS getPayment( Integer id){
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        return optionalPayment.map(this::transformToPaymentRS).orElse(null);
    }

    public List<PaymentRS> getPayments(){
        List<Payment> payments = paymentRepository.findAll();
        return this.transformToListPaymentRS(payments);
    }

    @Transactional
    public PaymentRS create(PaymentRQ paymentRQ){
        Payment newPayment = this.transformPaymentRQ(paymentRQ);
        Payment payment = paymentRepository.findByLoanIdAndLoanTransactionId(newPayment.getLoanId(),
                newPayment.getLoanTransactionId());
        if(payment != null){
            throw new RuntimeException("Ya existe");
        }else{
            return this.transformToPaymentRS(paymentRepository.save(newPayment));
        }
    }

    @Transactional
    public PaymentRS update(PaymentRQ paymentRQ){
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentRQ.getId());
        if(paymentOptional.isPresent()){
            Payment paymentTmp = paymentOptional.get();
            Payment payment = this.transformPaymentRQ(paymentRQ);
            paymentTmp.setType(payment.getType());
            paymentTmp.setReference(payment.getReference());
            return this.transformToPaymentRS(paymentRepository.save(paymentTmp));
        }else{
            throw new RuntimeException("Pago no encontrado");
        }
    }

    private List<PaymentRS> transformToListPaymentRS(List<Payment> payments){
        List<PaymentRS> paymentRSList = new ArrayList<>();
        for(Payment payment : payments){
            PaymentRS paymentRS = PaymentRS.builder()
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
            paymentRSList.add(paymentRS);
        }
        return paymentRSList;
    }

    //Response
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

    //Request
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
}