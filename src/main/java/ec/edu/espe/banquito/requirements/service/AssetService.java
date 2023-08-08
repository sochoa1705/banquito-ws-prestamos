package ec.edu.espe.banquito.requirements.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ec.edu.espe.banquito.requirements.controller.DTO.AssetRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.AssetRS;
import ec.edu.espe.banquito.requirements.controller.DTO.AssetUpdateRQ;
import ec.edu.espe.banquito.requirements.model.Asset;
import ec.edu.espe.banquito.requirements.repository.AssetRepository;
import jakarta.transaction.Transactional;

@Service
public class AssetService {
    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public AssetRS obtain(Integer id) {
        Optional<Asset> assetOpt = this.assetRepository.findById(id);
        if (assetOpt.isPresent()) {
            return this.transformAsset(assetOpt.get());
        } else {
            return null;
        }
    }

    public List<AssetRS> getAllAssets() {
        List<Asset> assets = this.assetRepository.findAll();
        List<AssetRS> assetsList = new ArrayList<>();
        for (Asset asset : assets) {
            assetsList.add(this.transformAsset(asset));
        }
        return assetsList;
    }

    @Transactional
    public AssetRS createAsset(AssetRQ assetRQ) {
        try {
            Asset asset = this.transformAssetRQ(assetRQ);
            this.assetRepository.save(asset);
            return this.transformAsset(asset);
        } catch (RuntimeException rte) {
            throw new RuntimeException("Error al crear el activo: " + rte.getMessage(), rte);
        }
    }

    @Transactional
    public AssetRS updateAsset(AssetUpdateRQ rq) {

        Optional<Asset> assetOpt = this.assetRepository.findById(rq.getId());

        if (assetOpt.isPresent()) {
            Asset assetTmp = assetOpt.get();

            Asset asset = this.transformOfAssetUpdateRQ(rq);

            assetTmp.setAmount(asset.getAmount());
            assetTmp.setCurrency(asset.getCurrency());
            assetTmp.setGuarantorCode(asset.getGuarantorCode());
            assetTmp.setGuarantorType(asset.getGuarantorType());

            this.assetRepository.save(assetTmp);

            return this.transformAsset(asset);
        } else {
            throw new RuntimeException("Activo no encontrado");
        }
    }

    @Transactional
    public void deleteAsset(Integer id) {
        try {
            Optional<Asset> assetOpt = this.assetRepository.findById(id);
            if (assetOpt.isPresent()) {
                this.assetRepository.delete(assetOpt.get());
            } else {
                throw new RuntimeException("El activo no esta registrado: " + id);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("No se puede eliminar el activo con Codigo: " + id, rte);
        }
    }

    @Transactional
    public AssetRS logicDelete(Integer id) {
        try {
            Optional<Asset> assetLogicDeleteOpt = this.assetRepository.findById(id);
            if (assetLogicDeleteOpt.isPresent()) {

                Asset assetTmp = assetLogicDeleteOpt.get();
                assetTmp.setStatus("INA");
                this.assetRepository.save(assetTmp);

                return this.transformAsset(assetTmp);

            } else {
                throw new RuntimeException("Activo no encontrado: " + id);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("Activo no encontrado y no se puede eliminar: " + id, rte);
        }
    }

    private AssetRS transformAsset(Asset asset) {
        AssetRS assetRS = AssetRS
                .builder()
                .id(asset.getId())
                .amount(asset.getAmount())
                .guarantorCode(asset.getGuarantorCode())
                .guarantorType(asset.getGuarantorType())
                .currency(asset.getCurrency())
                .build();
        return assetRS;
    }

    private Asset transformAssetRQ(AssetRQ rq) {
        Asset branch = Asset
                .builder()
                .amount(rq.getAmount())
                .guarantorCode(rq.getGuarantorCode())
                .guarantorType(rq.getGuarantorType())
                .currency(rq.getCurrency())
                .build();
        return branch;
    }

    private Asset transformOfAssetUpdateRQ(AssetUpdateRQ rq) {
        Asset branch = Asset
                .builder()
                .id(rq.getId())
                .amount(rq.getAmount())
                .guarantorCode(rq.getGuarantorCode())
                .guarantorType(rq.getGuarantorType())
                .currency(rq.getCurrency())
                .build();
        return branch;
    }
}
