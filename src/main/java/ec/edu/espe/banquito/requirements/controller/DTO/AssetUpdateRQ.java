package ec.edu.espe.banquito.requirements.controller.DTO;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssetUpdateRQ {

    private Integer id;
    private BigDecimal amount;
    private String guarantorCode;
    private String guarantorType;
    private String currency;

}
