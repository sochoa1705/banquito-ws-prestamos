package ec.edu.espe.banquito.requirements.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Loan;


public interface LoanRepository extends JpaRepository<Loan, Long> {

    Loan findByGroupCompanyId(Integer groupCompanyId);
    Loan findByCustomerId(Integer customerId);
    Loan findByUniqueKey(String uniqueId);
    Loan findByCustomerIdAndLoanProductId(Integer customerId, Integer loanProductId);
    Loan findByGroupCompanyIdAndLoanProductId(Integer groupCompanyId, Integer loanProductId);

    Loan findByGroupCompanyIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(Integer groupCompanyId,
                                                                                Integer loanProductId,
                                                                                String loanHolderType,
                                                                                String loanHolderCode);

    Loan findByCustomerIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(Integer customerId,
                                                                                Integer loanProductId,
                                                                                String loanHolderType,
                                                                                String loanHolderCode);
}
