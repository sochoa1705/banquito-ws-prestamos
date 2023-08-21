package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;
import java.security.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanTransactionRQ {

    private String type;
    private BigDecimal amount;
    private Boolean applyTax;
    private String notes;

}