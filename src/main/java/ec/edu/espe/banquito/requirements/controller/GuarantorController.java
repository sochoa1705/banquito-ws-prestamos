package ec.edu.espe.banquito.requirements.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.banquito.requirements.controller.DTO.GuarantorRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.GuarantorRS;
import ec.edu.espe.banquito.requirements.service.GuarantorService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/instrumentation/guarantor")
public class GuarantorController {

    private final GuarantorService guarantorService;

    public GuarantorController(GuarantorService guarantorService) {
        this.guarantorService = guarantorService;
    }

    @GetMapping
    public ResponseEntity<?> getGuarantorByCode(@RequestParam String guarantorCode,
                                                @RequestParam String guarantorType) {
        try {

            return ResponseEntity.ok(this.guarantorService.obtainByCodeAndType(guarantorCode, guarantorType));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createGuarantor(@RequestBody GuarantorRQ guarantorRQ) {
        try {
            return ResponseEntity.ok(this.guarantorService.create(guarantorRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<GuarantorRS> delete(@RequestParam String guarantorCode,
                                              @RequestParam String guarantorType) {
        try{
            GuarantorRS rs = this.guarantorService.logicDelete(guarantorCode, guarantorType);
            return ResponseEntity.ok().body(rs);
        }catch (RuntimeException rte){
            throw new RuntimeException("Garante no encontrado y no puede ser eliminado: " + guarantorCode, rte);
        }
    }

}
