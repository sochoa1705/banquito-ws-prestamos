package ec.edu.espe.banquito.requirements.controller.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class LoanProcessRS {

    //Guarantor
    private Integer guarantorId;
    private String guarantorCode;
    private String guarantorType;
    private String guarantorName;
    private String guarantorStatus;
    //Asset
    private Integer assetId;
    private BigDecimal assetAmount;
    private String assetType;
    private String assetName;
    private String assetCurrency;
    private String assetStatus;
    //Loan
    private Integer loanId;
    private Integer groupCompanyId;
    private Integer customerId;
    private Integer branchId;
    private Integer loanProductId;
    private String loanUniqueKey;
    private String loanHolderType;
    private String loanHolderCode;
    private String loanName;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private Integer loanGracePeriod;
    private String loanGracePeriodType;
    private String loanStatus;
    private Date loanApprovalDate;
    private Date loanDueDate;
    private Date loanLastPaymentDueDate;
    private Integer loanDaysLate;
    private BigDecimal loanInterestRate;
    private BigDecimal loanQuote;
    //Payment
    private Integer paymentId;
    private Integer loanTransactionId;
    private Integer accountTransactionId;
    private String paymentType;
    private String paymentReference;
    private String paymentStatus;
    private String paymentCreditorBankCode;
    private String paymentCreditorAccount;
    private String paymentDebtorAccount;
    private String paymentDebtorBankCode;
    private Date paymentDueDate;
    //LoanTransaction
    private String loanTransactionUniqueKey;
    private String loanTransactionType;
    private Date loanTransactionCreationDate;
    private Date loanTransactionBookingDate;
    private Date loanTransactionValueDate;
    private String loanTransactionStatus;
    private BigDecimal loanTransactionAmount;
    private Boolean loanTransactionApplyTax;
    private String loanTransactionParentLoanTrxKey;
    private String loanTransactionNotes;

}
