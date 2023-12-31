package ec.edu.espe.banquito.requirements.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LOAN")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOAN_ID", nullable = false)
    private Integer id;

    @Column(name = "GROUP_COMPANY_ID", length = 36)
    private String groupCompanyId;

    @Column(name = "CUSTOMER_ID", length = 36)
    private String customerId;

    @Column(name = "GUARANTOR_ID")
    private Integer guarantorId;

    @Column(name = "BRANCH_ID", length = 36)
    private String branchId;

    @Column(name = "LOAN_PRODUCT_ID", nullable = false, length = 36)
    private String loanProductId;

    @Column(name = "ASSET_ID")
    private Integer assetId;

    @Column(name = "UNIQUE_KEY", nullable = false, length = 36)
    private String uniqueKey;

    @Column(name = "LOAN_HOLDER_TYPE", nullable = false, length = 3)
    private String loanHolderType;

    @Column(name = "LOAN_HOLDER_CODE", nullable = false, length = 32)
    private String loanHolderCode;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "TERM", nullable = false)
    private Integer term;

    @Column(name = "GRACE_PERIOD", nullable = false)
    private Integer gracePeriod;

    @Column(name = "GRACE_PERIOD_TYPE", nullable = false, length = 10)
    private String gracePeriodType;

    @Column(name = "STATUS", length = 3)
    private String status;

    @Temporal(TemporalType.DATE)
    @Column(name = "APPROVAL_DATE", nullable = false)
    private Date approvalDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "DUE_DATE", nullable = false)
    private Date dueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_PAYMENT_DUE_DATE")
    private Date lastPaymentDueDate;

    @Column(name = "DAYS_LATE")
    private Integer daysLate;

    @Column(name = "INTEREST_RATE", precision = 18, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(name = "QUOTE", precision = 18, scale = 2, nullable = false)
    private BigDecimal quote;

    @Version
    @Column(name = "VERSION" , nullable = false)
    private Long version;

    //Relacion Group Company

    //Relacion Customer

    //@ManyToOne
    //@JoinColumn(name = "INTEREST_ACCRUE_ID", referencedColumnName = "INTEREST_ACCRUE_ID", insertable = false, updatable = false, nullable = false)
    //private InterestAccrue interestAccrue;

    @ManyToOne
    @JoinColumn(name = "GUARANTOR_ID", referencedColumnName = "GUARANTOR_ID", insertable = false, updatable = false, nullable = false)
    private Guarantor guarantor;

    //Relacion Branch

    //Relacion Loan Product

    @ManyToOne
    @JoinColumn(name = "ASSET_ID", referencedColumnName = "ASSET_ID", insertable = false, updatable = false, nullable = false)
    private Asset asset;

}
