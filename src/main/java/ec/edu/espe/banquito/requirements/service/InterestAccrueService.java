package ec.edu.espe.banquito.requirements.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ec.edu.espe.banquito.requirements.controller.DTO.InterestAccrueRQ;
import ec.edu.espe.banquito.requirements.controller.DTO.InterestAccrueRS;
import ec.edu.espe.banquito.requirements.controller.DTO.InterestAccrueUpdateRQ;
import ec.edu.espe.banquito.requirements.model.InterestAccrue;
import ec.edu.espe.banquito.requirements.repository.InterestAccrueRepository;
import jakarta.transaction.Transactional;

@Service
public class InterestAccrueService {
    private final InterestAccrueRepository interestAccrueRepository;

    public InterestAccrueService(InterestAccrueRepository interestAccrueRepository) {
        this.interestAccrueRepository = interestAccrueRepository;
    }

    public InterestAccrueRS obtain(Integer id) {
        Optional<InterestAccrue> interestAccrueOpt = this.interestAccrueRepository.findById(id);
        if (interestAccrueOpt.isPresent()) {
            return this.transformInterestAccrue(interestAccrueOpt.get());
        } else {
            return null;
        }
    }

    public List<InterestAccrueRS> getAllInterestAccrue() {
        List<InterestAccrue> interests = this.interestAccrueRepository.findAll();
        List<InterestAccrueRS> interestsList = new ArrayList<>();
        for (InterestAccrue interest : interests) {
            interestsList.add(this.transformInterestAccrue(interest));
        }
        return interestsList;
    }

    @Transactional
    public InterestAccrueRS createInterest(InterestAccrueRQ interestRQ) {
        try {
            InterestAccrue interest = this.transformInterestAccrueRQ(interestRQ);
            this.interestAccrueRepository.save(interest);
            return this.transformInterestAccrue(interest);
        } catch (RuntimeException rte) {
            throw new RuntimeException("Error al crear el interés: " + rte.getMessage(), rte);
        }
    }

    @Transactional
    public InterestAccrueRS updateInterest(InterestAccrueUpdateRQ rq) {

        Optional<InterestAccrue> interestOpt = this.interestAccrueRepository.findById(rq.getId());

        if (interestOpt.isPresent()) {
            InterestAccrue interestTmp = interestOpt.get();

            InterestAccrue interest = this.transformOfInterestUpdateRQ(rq);

            interestTmp.setInterestRate(interest.getInterestRate());
            interestTmp.setInterestType(interest.getInterestType());
            interestTmp.setSpread(interest.getSpread());
            interestTmp.setChargeFrecuency(interest.getChargeFrecuency());

            this.interestAccrueRepository.save(interestTmp);

            return this.transformInterestAccrue(interest);
        } else {
            throw new RuntimeException("Acumulación de intereses no encontrado");
        }
    }

    @Transactional
    public void deleteInterest(Integer id) {
        try {
            Optional<InterestAccrue> interestAccrueOpt = this.interestAccrueRepository.findById(id);
            if (interestAccrueOpt.isPresent()) {
                this.interestAccrueRepository.delete(interestAccrueOpt.get());
            } else {
                throw new RuntimeException("La acumulación de intereses no esta registrada: " + id);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("No se puede eliminar la acumulación de intereses con Codigo: " + id, rte);
        }
    }

    @Transactional
    public InterestAccrueRS logicDelete(Integer id) {
        try {
            Optional<InterestAccrue> interestLogicDeleteOpt = this.interestAccrueRepository.findById(id);
            if (interestLogicDeleteOpt.isPresent()) {
                InterestAccrue interestTmp = interestLogicDeleteOpt.get();

                interestTmp.setStatus("INA");
                this.interestAccrueRepository.save(interestTmp);
                return this.transformInterestAccrue(interestTmp);

            } else {
                throw new RuntimeException("Interés no encontrado: " + id);
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("Interés no encontrado y no puede ser eliminado: " + id, rte);
        }
    }

    private InterestAccrue transformInterestAccrueRQ(InterestAccrueRQ rq) {
        InterestAccrue branch = InterestAccrue
                .builder()
                .interestRate(rq.getInterestRate())
                .interestType(rq.getInterestType())
                .spread(rq.getSpread())
                .chargeFrecuency(rq.getChargeFrecuency())
                .build();
        return branch;
    }

    private InterestAccrueRS transformInterestAccrue(InterestAccrue interest) {
        InterestAccrueRS interestRS = InterestAccrueRS
                .builder()
                .id(interest.getId())
                .interestRate(interest.getInterestRate())
                .interestType(interest.getInterestType())
                .spread(interest.getSpread())
                .chargeFrecuency(interest.getChargeFrecuency())
                .build();
        return interestRS;
    }

    private InterestAccrue transformOfInterestUpdateRQ(InterestAccrueUpdateRQ rq) {
        InterestAccrue branch = InterestAccrue
                .builder()
                .id(rq.getId())
                .interestRate(rq.getInterestRate())
                .interestType(rq.getInterestType())
                .spread(rq.getSpread())
                .chargeFrecuency(rq.getChargeFrecuency())
                .build();
        return branch;
    }

}
