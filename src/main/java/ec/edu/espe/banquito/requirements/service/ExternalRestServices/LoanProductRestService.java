package ec.edu.espe.banquito.requirements.service.ExternalRestServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LoanProductRestService {
    private final RestTemplate restTemplate;


}
