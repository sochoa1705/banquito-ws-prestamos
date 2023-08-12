package ec.edu.espe.banquito.requirements.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Payment;
import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment,Integer>{

    Optional<Payment> findByLoanId(Integer id);

    Optional<Payment> findByDebtorAccount(String debtorAccount);

    Optional<Payment> findByCreditorAccount(String creditorAccount);

    List<Payment> findByStatus(String status);

    Payment findByLoanIdAndLoanTransactionId(Integer loanId, Integer loanTransactionId);

}