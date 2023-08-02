package ec.edu.espe.banquito.requirements.controller.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuarantorRS {

    private Integer id;
    private String code;
    private String type;
    private String name;

}
