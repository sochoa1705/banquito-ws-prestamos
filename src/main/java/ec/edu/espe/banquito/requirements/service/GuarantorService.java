package ec.edu.espe.banquito.requirements.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ec.edu.espe.banquito.requirements.controller.DTO.GuarantorRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.GuarantorRS;
import ec.edu.espe.banquito.requirements.model.Guarantor;
import ec.edu.espe.banquito.requirements.repository.GuarantorRepository;
import jakarta.transaction.Transactional;

@Service
public class GuarantorService {

    private final GuarantorRepository guarantorRepository;

    public GuarantorService(GuarantorRepository guarantorRepository) {
        this.guarantorRepository = guarantorRepository;
    }

    public GuarantorRS obtainByCode(String code) {
        Guarantor guarantor = this.guarantorRepository.findByCode(code);
        return this.transformGuarantor(guarantor);
    }

    public GuarantorRS obtainByName(String nameGuarantor) {
        Guarantor guarantor = this.guarantorRepository.findByName(nameGuarantor);
        return this.transformGuarantor(guarantor);
    }

    public List<GuarantorRS> getAllGuarantors() {
        List<Guarantor> countries = this.guarantorRepository.findAll();
        List<GuarantorRS> countriesList = new ArrayList<>();
        for (Guarantor country : countries) {
            countriesList.add(this.transformGuarantor(country));
        }
        return countriesList;
    }

    @Transactional
    public GuarantorRS createGuarantor(GuarantorRQ guarantorRQ) {
        try {
            Guarantor guarantor = this.transformGuarantorRQ(guarantorRQ);
            this.guarantorRepository.save(guarantor);
            return this.transformGuarantor(guarantor);
        } catch (RuntimeException rte) {
            throw new RuntimeException("Error al crear el garante: " + rte.getMessage(), rte);
        }
    }

    @Transactional
    public GuarantorRS updateGuarantor(GuarantorRQ countryRQ) {

        Guarantor guarantor = this.transformGuarantorRQ(countryRQ);
        try {
            Guarantor guarantorUpdate = this.guarantorRepository.findByCode(guarantor.getCode());
            if (guarantorUpdate == null) {
                throw new RuntimeException("Garante " + guarantor.getName() + " no encontrando");
            } else {
                guarantorUpdate.setCode(guarantor.getCode());
                guarantorUpdate.setName(guarantor.getName());
                guarantorUpdate.setType(guarantor.getType());
                this.guarantorRepository.save(guarantorUpdate);

                return this.transformGuarantor(guarantorUpdate);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException(rte);
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            Optional<Guarantor> guarantorOpt = this.guarantorRepository.findById(id);
            if (guarantorOpt.isPresent()) {
                this.guarantorRepository.delete(guarantorOpt.get());
            } else {
                throw new RuntimeException("El garante no esta registrado: " + id);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("No se puede eliminar el garante con código: " + id, rte);
        }
    }

    @Transactional
    public GuarantorRS logicDelete(String code){
        try {
            Guarantor countryLogicDelete = this.guarantorRepository.findByCode(code);
            if(countryLogicDelete != null) {
                countryLogicDelete.setStatus("INA");
                this.guarantorRepository.save(countryLogicDelete);
                return this.transformGuarantor(countryLogicDelete);
            }else {
                throw new RuntimeException("Garante no encontrado: " + code);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("Garante no encontrado y no puede ser eliminado: " + code, rte);
        }
    }

    private Guarantor transformGuarantorRQ(GuarantorRQ rq) {
        Guarantor guarantor = Guarantor
                .builder()
                .name(rq.getName())
                .type(rq.getType())
                .code(rq.getCode())
                .build();
        return guarantor;
    }

    private GuarantorRS transformGuarantor(Guarantor guarantor) {
        GuarantorRS guarantorRS = GuarantorRS
                .builder()
                .id(guarantor.getId())
                .name(guarantor.getName())
                .type(guarantor.getType())
                .code(guarantor.getCode())
                .build();
        return guarantorRS;
    }

}
