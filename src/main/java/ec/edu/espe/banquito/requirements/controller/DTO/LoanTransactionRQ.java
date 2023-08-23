package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanTransactionRQ {

    private String uniqueKey;
    private String type;
    private String status;
    private BigDecimal amount;
    private Boolean applyTax;
    private String parentLoanTrxKey;
    private String notes;

}
