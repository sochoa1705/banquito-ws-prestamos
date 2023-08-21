package ec.edu.espe.banquito.requirements.controller;


import ec.edu.espe.banquito.requirements.controller.DTO.LoanRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.LoanRS;
import ec.edu.espe.banquito.requirements.controller.DTO.LoanTransactionRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.PaymentRQ;
import ec.edu.espe.banquito.requirements.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/requirements/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("customerId/{customerId}")
    public ResponseEntity<LoanRS> getLoanByCustomerId(@PathVariable Integer customerId) {
        LoanRS rs = this.loanService.getLoanByCustomerId(customerId);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("groupCompanyId/{groupCompanyId}")
    public ResponseEntity<LoanRS> getLoanByGroupCompanyId(@PathVariable Integer groupCompanyId) {
        LoanRS rs = this.loanService.getLoanByGroupCompanyId(groupCompanyId);
        return ResponseEntity.ok(rs);
    }

    @PostMapping("/loan-create")
    public ResponseEntity<?> createLoan(@RequestBody Map<String, Object> objetos){
        LoanRQ loanRQ = (LoanRQ) objetos.get("loanRQ");
        LoanTransactionRQ loanTransactionRQ = (LoanTransactionRQ) objetos.get("loanTransactionRQ");
        PaymentRQ paymentRQ = (PaymentRQ) objetos.get("paymentRQ");
        try{
            return ResponseEntity.ok(this.loanService.create(loanRQ, paymentRQ, loanTransactionRQ));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

}
