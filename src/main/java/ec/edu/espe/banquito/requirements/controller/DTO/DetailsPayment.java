package ec.edu.espe.banquito.requirements.controller.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class DetailsPayment {

    //Loan
    private String loanProductId;
    private String name;
    private Date lastPaymentDueDate;

    //Payment
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

    //LoanTransaction
    private String uniqueKey;
    private Date creationDate;
    private Date bookingDate;
    private Date valueDate;
    private BigDecimal amount;
    private Boolean applyTax;
    private String parentLoanTrxKey;
    private String notes;

}
