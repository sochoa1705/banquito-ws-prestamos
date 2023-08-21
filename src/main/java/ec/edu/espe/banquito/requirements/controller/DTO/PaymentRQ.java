package ec.edu.espe.banquito.requirements.controller.DTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRQ {

    private Integer id;
    private Integer loanId;
    private Integer loanTransactionId;
    private Integer accountTransactionId;
    private String type;
    private String reference;
    private String creditorAccount;
    private String debtorAccount;

}