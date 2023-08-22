package ec.edu.espe.banquito.requirements.controller.DTO;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PaymentRQ {

    private String creditorBankCode;
    private String creditorAccount;
    private String debtorAccount;
    private String debtorBankCode;

}