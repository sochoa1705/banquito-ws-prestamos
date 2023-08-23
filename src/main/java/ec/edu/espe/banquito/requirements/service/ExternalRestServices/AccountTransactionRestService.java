package ec.edu.espe.banquito.requirements.service.ExternalRestServices;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountTransactionRestService {

    private final RestTemplate restTemplate;

    public void sendAccountTransactionCreationRequest(String creditorAccount, String debtorAccount,
                                                      String transactionType, String parentTransactionKey,
                                                      Float amount, String reference){
        String url = "https://banquito-ws-cuentas-ntsumodxxq-uc.a.run.app/api/v1/account-transaction";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("creditorAccount", creditorAccount);
        requestData.put("debtorAccount", debtorAccount);
        requestData.put("transactionType", transactionType);
        requestData.put("parentTransactionKey", parentTransactionKey);
        requestData.put("amount", amount);
        requestData.put("reference", reference);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("Error al crear una transacción en el servicio externo");
        }
    }
}
