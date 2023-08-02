package ec.edu.espe.banquito.requirements.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByGroupCompanyId(Integer groupCompanyId);

    List<Loan> findByCustomerId(Integer customerId);
    
}
