package ec.edu.espe.banquito.requirements.model;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LOAN_TRANSACTION")
public class LoanTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOAN_TRANSACTION_ID", nullable = false)
    private Integer id;

    @Column(name = "UNIQUE_KEY", length = 36, nullable = false)
    private String uniqueKey;

    @Column(name = "TYPE", length = 12, nullable = false)
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BOOKING_DATE", nullable = false)
    private Date bookingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VALUE_DATE", nullable = false)
    private Date valueDate;

    @Column(name = "STATUS", length = 3, nullable = false)
    private String status;

    @Column(name = "AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "APPLY_TAX", nullable = false)
    private Boolean applyTax;

    @Column(name = "PARENT_LOAN_TRX_KEY", length = 36, nullable = false)
    private String parentLoanTrxKey;

    @Column(name = "NOTES", length = 200, nullable = false)
    private String notes;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @JoinColumn(name = "LOAN_TRANSACTION_ID", referencedColumnName = "LOAN_TRANSACTION_ID")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Payment> payments;
}