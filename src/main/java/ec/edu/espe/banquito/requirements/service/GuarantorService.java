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

    public GuarantorRS obtainByCodeAndType(String code, String type) {
        Guarantor guarantor = this.guarantorRepository.findByCodeAndType(code, type);
        return this.transformGuarantor(guarantor);
    }

    public GuarantorRS obtainByName(String nameGuarantor) {
        Guarantor guarantor = this.guarantorRepository.findByName(nameGuarantor);
        return this.transformGuarantor(guarantor);
    }
    //No se usa
    public List<GuarantorRS> getAllGuarantors() {
        List<Guarantor> countries = this.guarantorRepository.findAll();
        List<GuarantorRS> countriesList = new ArrayList<>();
        for (Guarantor country : countries) {
            countriesList.add(this.transformGuarantor(country));
        }
        return countriesList;
    }

    @Transactional
    public GuarantorRS create(GuarantorRQ guarantorRQ) {
        Guarantor newGuarantor = this.transformGuarantorRQ(guarantorRQ);
        Guarantor existingGuarantor = this.guarantorRepository.findByCodeAndTypeAndNameAndStatus(
                newGuarantor.getCode(), newGuarantor.getType(), newGuarantor.getName(), "ACT"
        );
        if(existingGuarantor == null){
            newGuarantor.setStatus("ACT");
            return this.transformGuarantor(this.guarantorRepository.save(newGuarantor));
        }else{
            throw new RuntimeException("Garante ya existe");
        }
    }

    //No se usa
    @Transactional
    public GuarantorRS updateGuarantor(GuarantorRQ countryRQ) {

        Guarantor guarantor = this.transformGuarantorRQ(countryRQ);
        try {
            Guarantor guarantorUpdate = this.guarantorRepository.findByCodeAndType(
                    guarantor.getCode(),
                    guarantor.getType()
            );
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

    //No se usa
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
    public GuarantorRS logicDelete(String code, String type){
        try {
            Guarantor guarantor = this.guarantorRepository.findByCodeAndType(code, type);
            if(guarantor != null) {
                guarantor.setStatus("INA");
                this.guarantorRepository.save(guarantor);
                return this.transformGuarantor(guarantor);
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
