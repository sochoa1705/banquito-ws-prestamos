package ec.edu.espe.banquito.requirements.controller.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PaymentRS {

    private Integer id;
    private Integer loanId;
    private Integer loanTransactionId;
    private String accountTransactionId;
    private String type;
    private String reference;
    private String status;
    private String creditorBankCode;
    private String creditorAccount;
    private String debtorAccount;
    private String debtorBankCode;
    private Date dueDate;

}