package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanTransactionRS {

    private Integer id;
    private String uniqueKey;
    private String type;
    private Date creationDate;
    private Date bookingDate;
    private Date valueDate;
    private String status;
    private BigDecimal amount;
    private Boolean applyTax;
    private String parentLoanTrxKey;
    private String notes;
}