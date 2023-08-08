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

import ec.edu.espe.banquito.requirements.controller.DTO.GuarantorRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.GuarantorRS;
import ec.edu.espe.banquito.requirements.service.GuarantorService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/requirements/guarantor")
public class GuarantorController {

    private final GuarantorService guarantorService;

    public GuarantorController(GuarantorService guarantorService) {
        this.guarantorService = guarantorService;
    }

    @GetMapping("/{guarantorCode}")
    public ResponseEntity<?> getGuarantorByCode(@PathVariable String guarantorCode) {
        try {

            return ResponseEntity.ok(this.guarantorService.obtainByCode(guarantorCode));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/guarantors/{guarantorName}")
    public ResponseEntity<?> getGuarantorByName(@PathVariable String guarantorName) {
        try {
            String name = java.net.URLDecoder.decode(guarantorName, "UTF-8");
            return ResponseEntity.ok(this.guarantorService.obtainByName(name));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GuarantorRS>> getAllGuarantors() {
        List<GuarantorRS> guarantors = this.guarantorService.getAllGuarantors();
        return ResponseEntity.ok(guarantors);
    }

    @PostMapping
    public ResponseEntity<?> createGuarantor(@RequestBody GuarantorRQ guarantorRQ) {
        try {
            return ResponseEntity.ok(this.guarantorService.createGuarantor(guarantorRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<GuarantorRS> updateGuarantor(@RequestBody GuarantorRQ guarantorRQ) {
        try {
            GuarantorRS rs = this.guarantorService.updateGuarantor(guarantorRQ);
            return ResponseEntity.ok(rs);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }

    // @DeleteMapping("/{guarantorId}")
    // public ResponseEntity<String> deleteGuarantor(@PathVariable Integer guarantorId) {
    //     try {
    //         guarantorService.delete(guarantorId);
    //         return ResponseEntity.ok("Garante eliminado exitosamente.");
    //     } catch (RuntimeException rte) {
    //         return ResponseEntity.badRequest().body("No se pudo eliminar el garante: " + rte.getMessage());
    //     }
    // }

    @DeleteMapping("/{guarantorCode}")
    public ResponseEntity<GuarantorRS> delete(@PathVariable String guarantorCode) {
        try{
            GuarantorRS rs = this.guarantorService.logicDelete(guarantorCode);
            return ResponseEntity.ok().body(rs);
        }catch (RuntimeException rte){
            throw new RuntimeException("Garante no encontrado y no puede ser eliminado: " + guarantorCode, rte);
        }
    }

}
