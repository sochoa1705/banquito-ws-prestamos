package ec.edu.espe.banquito.requirements.service.ExternalRestServices;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LoanProductRestService {
    private final RestTemplate restTemplate;
    public String sendObtainNameProductRequest(String uniqueKey) {
        String url = "https://banquito-ws-productos-activos-ntsumodxxq-uc.a.run.app/api/v1/loan-product/name/" + uniqueKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener la información del producto desde el servicio externo");
        }
        return response.getBody();
    }
}
