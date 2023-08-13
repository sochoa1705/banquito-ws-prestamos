package ec.edu.espe.banquito.requirements.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.LoanTransaction;
import java.util.List;
import java.util.Optional;
import java.security.Timestamp;



public interface LoanTransactionRepository extends JpaRepository <LoanTransaction,Integer>{

    LoanTransaction  findByUniqueKey(String uniqueKey);

    List<LoanTransaction> findByBookingDate(Timestamp bookingDate);

    List<LoanTransaction> findByCreationDate(Timestamp creationDate);

    List<LoanTransaction> findByStatus(String status);


}