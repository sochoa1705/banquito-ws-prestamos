package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssetRQ {

    private BigDecimal amount;
    private String guarantorCode;
    private String guarantorType;
    private String currency;

}
