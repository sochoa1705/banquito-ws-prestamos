package ec.edu.espe.banquito.requirements.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.requirements.model.Loan;


public interface LoanRepository extends JpaRepository<Loan, Integer> {

    Loan findByGroupCompanyId(String groupCompanyId);
    Loan findByCustomerId(String customerId);
    Loan findByUniqueKey(String uniqueId);
    Loan findByCustomerIdAndLoanProductId(String customerId, String loanProductId);
    Loan findByGroupCompanyIdAndLoanProductId(String groupCompanyId, String loanProductId);

    Loan findByGroupCompanyIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(String groupCompanyId,
                                                                                String loanProductId,
                                                                                String loanHolderType,
                                                                                String loanHolderCode);

    Loan findByCustomerIdAndLoanProductIdAndLoanHolderTypeAndLoanHolderCode(String customerId,
                                                                            String loanProductId,
                                                                                String loanHolderType,
                                                                                String loanHolderCode);
}
