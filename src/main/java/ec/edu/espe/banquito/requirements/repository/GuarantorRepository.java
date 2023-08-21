package ec.edu.espe.banquito.requirements.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Guarantor;

public interface GuarantorRepository extends JpaRepository<Guarantor, Integer> {

    Guarantor findByCodeAndType(String code, String type);

    Guarantor findByCodeAndTypeAndNameAndStatus(String code, String type, String name, String status);

    Guarantor findByName(String name);

    List<Guarantor> findByType(String type);

    @Override
    List<Guarantor> findAll();

}
