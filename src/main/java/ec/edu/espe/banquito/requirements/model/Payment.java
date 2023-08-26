package ec.edu.espe.banquito.requirements.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID", nullable = false)
    private Integer id;

    @Column(name = "LOAN_ID", nullable = false)
    private Integer loanId;

    @Column(name = "LOAN_TRANSACTION_ID", nullable = false)
    private Integer loanTransactionId;

    @Column(name = "ACCOUNT_TRANSACTION_ID", length = 36)
    private String accountTransactionId;

    @Column(name = "TYPE", length = 3, nullable = false)
    private String type;

    @Column(name = "REFERENCE", length = 50, nullable = false)
    private String reference;

    @Column(name = "STATUS", length = 3, nullable = false)
    private String status;

    @Column(name = "CREDITOR_BANK_CODE", length = 20)
    private String creditorBankCode;

    @Column(name = "CREDITOR_ACCOUNT", length = 16)
    private String creditorAccount;

    @Column(name = "DEBTOR_ACCOUNT", length = 16)
    private String debtorAccount;

    @Column(name = "DEBTOR_BANK_CODE", length = 20)
    private String debtorBankCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "DUE_DATE", nullable = false)
    private Date dueDate;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "LOAN_ID", nullable = false, updatable = false, insertable = false)
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "LOAN_TRANSACTION_ID", nullable = false, updatable = false, insertable = false)
    private LoanTransaction loanTransaction;
}