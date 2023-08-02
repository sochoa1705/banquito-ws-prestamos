package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterestAccrueRQ {

    private BigDecimal interestRate;
    private String interestType;
    private BigDecimal spread;
    private String chargeFrecuency;

}
