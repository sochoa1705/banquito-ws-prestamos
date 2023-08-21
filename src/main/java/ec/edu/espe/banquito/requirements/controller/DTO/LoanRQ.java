package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanRQ {

    private Integer groupCompanyId;
    private Integer customerId;
    private Integer guarantorId;
    private Integer branchId;
    private Integer loanProductId;
    private Integer assetId;
    private String loanHolderType;
    private String loanHolderCode;
    private String name;
    private BigDecimal amount;
    private Integer term;
    private Integer gracePeriod;
    private String gracePeriodType;
    private BigDecimal monthlyFee;
    private BigDecimal interestRate;
}
