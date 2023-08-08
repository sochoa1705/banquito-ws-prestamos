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

import ec.edu.espe.banquito.requirements.controller.DTO.AssetRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.AssetRS;
import ec.edu.espe.banquito.requirements.controller.DTO.AssetUpdateRQ;
import ec.edu.espe.banquito.requirements.service.AssetService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/requirements/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<List<AssetRS>> getAll() {
        List<AssetRS> assets = this.assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<AssetRS> obtain(@PathVariable Integer assetId) {
        AssetRS rs = this.assetService.obtain(assetId);
        return ResponseEntity.ok(rs);
    }

    @PostMapping
    public ResponseEntity<?> createAsset(@RequestBody AssetRQ assetRQ) {
        try {
            return ResponseEntity.ok(this.assetService.createAsset(assetRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody AssetUpdateRQ asset) {
        try {
            return ResponseEntity.ok(this.assetService.updateAsset(asset));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @DeleteMapping("/{assetId}")
    // public ResponseEntity<String> deleteAsset(@PathVariable Integer assetId) {
    //     try {
    //         this.assetService.deleteAsset(assetId);
    //         return ResponseEntity.ok("Activo eliminado correctamente");
    //     } catch (RuntimeException rte) {
    //         return ResponseEntity.badRequest().body("No se pudo eliminar el activo: " + rte.getMessage());
    //     }
    // }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<AssetRS> delete(@PathVariable Integer assetId) {
        try{
            AssetRS rs = this.assetService.logicDelete(assetId);
            return ResponseEntity.ok().body(rs);
        }catch (RuntimeException rte){
            throw new RuntimeException("Activo no encontrado y no puede ser eliminado: " + assetId, rte);
        }
    }
}
