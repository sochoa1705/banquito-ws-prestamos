package ec.edu.espe.banquito.requirements.controller;


import ec.edu.espe.banquito.requirements.controller.DTO.*;
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
    public ResponseEntity<LoanRS> getLoanByCustomerId(@PathVariable String customerId) {
        LoanRS rs = this.loanService.getLoanByCustomerId(customerId);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("groupCompanyId/{groupCompanyId}")
    public ResponseEntity<LoanRS> getLoanByGroupCompanyId(@PathVariable String groupCompanyId) {
        LoanRS rs = this.loanService.getLoanByGroupCompanyId(groupCompanyId);
        return ResponseEntity.ok(rs);
    }

    @PostMapping("/loan-create")
    public ResponseEntity<?> createLoan(@RequestBody LoanProcessRQ loanProcessRQ){
        try{
            return ResponseEntity.ok(this.loanService.create(loanProcessRQ));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().body(rte.getMessage());
        }
    }

}
