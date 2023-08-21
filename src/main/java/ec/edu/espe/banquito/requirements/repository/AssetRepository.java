package ec.edu.espe.banquito.requirements.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Asset;

import java.math.BigDecimal;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    Asset findByAmountAndGuarantorCodeAndGuarantorTypeAndCurrency(BigDecimal amount, String guarantorCode,
                                                                  String GuarantorType, String Currency);
}
