package ec.edu.espe.banquito.requirements.controller.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class LoanProcessRQ {

    //Guarantor
    private String guarantorCode;
    private String guarantorType;
    private String guarantorName;
    //Asset
    private BigDecimal assetAmount;
    private String assetType;
    private String assetName;
    private String assetCurrency;
    //Loan
    private Integer groupCompanyId;
    private Integer customerId;
    private Integer branchId;
    private Integer loanProductId;
    private String loanHolderType;
    private String loanHolderCode;
    private String loanName;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private Integer loanGracePeriod;
    private String loanGracePeriodType;
    private BigDecimal loanInterestRate;
    private BigDecimal loanQuote;
    //Payment
    private String paymentCreditorBankCode;
    private String paymentCreditorAccount;
    private String paymentDebtorAccount;
    private String paymentDebtorBankCode;
    //LoanTransaction

}
