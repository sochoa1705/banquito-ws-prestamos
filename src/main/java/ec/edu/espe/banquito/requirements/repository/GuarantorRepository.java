package ec.edu.espe.banquito.requirements.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Guarantor;

public interface GuarantorRepository extends JpaRepository<Guarantor, Integer> {

    Optional<Guarantor> findByCode(String code);

    List<Guarantor> findByName(String name);

    List<Guarantor> findByType(String type);

    List<Guarantor> findByTypeAndName(String type, String name);

}
