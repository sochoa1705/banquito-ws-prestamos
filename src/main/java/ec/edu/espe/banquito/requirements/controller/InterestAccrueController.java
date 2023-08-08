package ec.edu.espe.banquito.requirements.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.banquito.requirements.controller.DTO.InterestAccrueRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.InterestAccrueRS;
import ec.edu.espe.banquito.requirements.controller.DTO.InterestAccrueUpdateRQ;
import ec.edu.espe.banquito.requirements.service.InterestAccrueService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/requirements/interest")
public class InterestAccrueController {

    private final InterestAccrueService interestService;

    public InterestAccrueController(InterestAccrueService interestService) {
        this.interestService = interestService;
    }

    @GetMapping
    public ResponseEntity<List<InterestAccrueRS>> getAll() {
        List<InterestAccrueRS> interests = this.interestService.getAllInterestAccrue();
        return ResponseEntity.ok(interests);
    }

    @GetMapping("/{interestId}")
    public ResponseEntity<InterestAccrueRS> obtain(@PathVariable Integer interestId) {
        InterestAccrueRS rs = this.interestService.obtain(interestId);
        return ResponseEntity.ok(rs);
    }

    @PostMapping
    public ResponseEntity<?> createInterest(@RequestBody InterestAccrueRQ interestRQ) {
        try {
            return ResponseEntity.ok(this.interestService.createInterest(interestRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody InterestAccrueUpdateRQ interestRQ) {
        try {
            return ResponseEntity.ok(this.interestService.updateInterest(interestRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @DeleteMapping("/{interestId}")
    // public ResponseEntity<String> deleteInterestAccrue(@PathVariable Integer interestId) {
    //     try {
    //         this.interestService.deleteInterest(interestId);
    //         return ResponseEntity.ok("Acumulación de intereses eliminado correctamente.");
    //     } catch (RuntimeException rte) {
    //         return ResponseEntity.badRequest().body("No se pudo eliminar el interés: " + rte.getMessage());
    //     }
    // }

    @DeleteMapping("/{interestId}")
    public ResponseEntity<InterestAccrueRS> delete(@PathVariable Integer interestId) {
        try{
            InterestAccrueRS rs = this.interestService.logicDelete(interestId);
            return ResponseEntity.ok().body(rs);
        }catch (RuntimeException rte){
            throw new RuntimeException("Interés no encontrado y no puede ser eliminado: " + interestId, rte);
        }
    }
}
