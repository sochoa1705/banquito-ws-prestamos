package ec.edu.espe.banquito.requirements.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Guarantor;

public interface GuarantorRepository extends JpaRepository<Guarantor, Integer> {

    Guarantor findByCode(String code);

    Guarantor findByName(String name);

    List<Guarantor> findByType(String type);

    @Override
    List<Guarantor> findAll();

}
