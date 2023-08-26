package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanRS {

    private String groupCompanyId;
    private String customerId;
    private Integer guarantorId;
    private String branchId;
    private String loanProductId;
    private Integer assetId;
    private String uniqueKey;
    private String loanHolderType;
    private String loanHolderCode;
    private String name;
    private BigDecimal amount;
    private Integer term;
    private Integer gracePeriod;
    private String gracePeriodType;
    private String status;
    private Date approvalDate;
    private Date dueDate;
    private Date lastPaymentDueDate;
    private Integer daysLate;
    private BigDecimal interestRate;
    private BigDecimal quote;

}
