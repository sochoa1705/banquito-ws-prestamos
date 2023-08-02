package ec.edu.espe.banquito.requirements.controller.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuarantorRQ {

    private String code;
    private String type;
    private String name;

}
