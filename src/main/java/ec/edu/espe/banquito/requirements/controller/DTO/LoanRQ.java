package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanRQ {

    private String groupCompanyId;
    private String customerId;
    private String branchId;
    private String loanProductId;
    private String loanHolderType;
    private String loanHolderCode;
    private String name;
    private BigDecimal amount;
    private Integer term;
    private Integer gracePeriod;
    private String gracePeriodType;
    private BigDecimal interestRate;
    private BigDecimal quote;

}
