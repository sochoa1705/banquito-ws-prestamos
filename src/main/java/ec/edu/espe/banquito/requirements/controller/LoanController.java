package ec.edu.espe.banquito.requirements.controller;


import ec.edu.espe.banquito.requirements.controller.DTO.LoanRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.LoanRS;
import ec.edu.espe.banquito.requirements.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/requirements/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<LoanRS>> getAll() {
        List<LoanRS> loans = this.loanService.getAllLoan();
        return ResponseEntity.ok(loans);
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
    public ResponseEntity<?> createLoan(@RequestBody LoanRQ loanRQ){
        try{
            return ResponseEntity.ok(this.loanService.createLoan(loanRQ));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/loan-update")
    public ResponseEntity<?> updateLoan(@RequestBody LoanRQ loanRQ){
        try{
            return ResponseEntity.ok(this.loanService.updateLoan(loanRQ));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/loan-status")
    public ResponseEntity<?> statusLoan(@RequestBody LoanRQ loanRQ){
        try{
            return ResponseEntity.ok(this.loanService.statusLoan(loanRQ));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

}
