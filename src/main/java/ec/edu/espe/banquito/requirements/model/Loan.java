package ec.edu.espe.banquito.requirements.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "LOAN")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOAN_ID", nullable = false)
    private Long id; // se debería usar Long, los demás usan Integer

    @Column(name = "GROUP_COMPANY_ID", nullable = false)
    private Integer groupCompanyId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private Integer customerId;

    @Column(name = "INTEREST_ACCRUE_ID", nullable = false)
    private Integer interestAccrueId;

    @Column(name = "GUARANTOR_ID", nullable = false)
    private Integer guarantorId;

    @Column(name = "BRANCH_ID", nullable = false)
    private Integer branchId;

    @Column(name = "LOAN_PRODUCT_ID", nullable = false)
    private Integer loanProductId;

    @Column(name = "ASSET_ID", nullable = false)
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

    @Column(name = "STATUS", nullable = false, length = 3)
    private String status;

    @Column(name = "APPROVAL_DATE", nullable = false)
    private Date approvalDate;

    @Column(name = "DUE_DATE", nullable = false)
    private Date dueDate;

    @Column(name = "MONTHLY_FEE", precision = 18, scale = 2, nullable = false)
    private BigDecimal monthlyFee;

    @Column(name = "DAYS_LATE", nullable = false)
    private Integer daysLate;

    @Column(name = "INTEREST_RATE", precision = 18, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(name = "REDRAW", nullable = false)
    private Boolean redraw;

    @Column(name = "REDRAW_BALANCE", precision = 18, scale = 2, nullable = false)
    private BigDecimal redrawBalance;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    //Relacion Group Company

    //Relacion Customer

    @ManyToOne
    @JoinColumn(name = "INTEREST_ACCRUE_ID", referencedColumnName = "INTEREST_ACCRUE_ID", insertable = false, updatable = false, nullable = false)
    private InterestAccrue interestAccrue;

    @ManyToOne
    @JoinColumn(name = "GUARANTOR_ID", referencedColumnName = "GUARANTOR_ID", insertable = false, updatable = false, nullable = false)
    private Guarantor guarantor;

    //Relacion Branch

    //Relacion Loan Product

    @ManyToOne
    @JoinColumn(name = "ASSET_ID", referencedColumnName = "ASSET_ID", insertable = false, updatable = false, nullable = false)
    private Asset asset;

    public Loan() {
    }

    public Loan(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGroupCompanyId() {
        return groupCompanyId;
    }

    public void setGroupCompanyId(Integer groupCompanyId) {
        this.groupCompanyId = groupCompanyId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getInterestAccrueId() {
        return interestAccrueId;
    }

    public void setInterestAccrueId(Integer interestAccrueId) {
        this.interestAccrueId = interestAccrueId;
    }

    public Integer getGuarantorId() {
        return guarantorId;
    }

    public void setGuarantorId(Integer guarantorId) {
        this.guarantorId = guarantorId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(Integer loanProductId) {
        this.loanProductId = loanProductId;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getLoanHolderType() {
        return loanHolderType;
    }

    public void setLoanHolderType(String loanHolderType) {
        this.loanHolderType = loanHolderType;
    }

    public String getLoanHolderCode() {
        return loanHolderCode;
    }

    public void setLoanHolderCode(String loanHolderCode) {
        this.loanHolderCode = loanHolderCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getGracePeriodType() {
        return gracePeriodType;
    }

    public void setGracePeriodType(String gracePeriodType) {
        this.gracePeriodType = gracePeriodType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Integer getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(Integer daysLate) {
        this.daysLate = daysLate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Boolean getRedraw() {
        return redraw;
    }

    public void setRedraw(Boolean redraw) {
        this.redraw = redraw;
    }

    public BigDecimal getRedrawBalance() {
        return redrawBalance;
    }

    public void setRedrawBalance(BigDecimal redrawBalance) {
        this.redrawBalance = redrawBalance;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Loan [id=" + id + ", groupCompanyId=" + groupCompanyId + ", customerId=" + customerId
                + ", interestAccrueId=" + interestAccrueId + ", guarantorId=" + guarantorId + ", branchId=" + branchId
                + ", loanProductId=" + loanProductId + ", assetId=" + assetId + ", uniqueKey=" + uniqueKey
                + ", loanHolderType=" + loanHolderType + ", loanHolderCode=" + loanHolderCode + ", name=" + name
                + ", amount=" + amount + ", term=" + term + ", gracePeriod=" + gracePeriod + ", gracePeriodType="
                + gracePeriodType + ", status=" + status + ", approvalDate=" + approvalDate + ", dueDate=" + dueDate
                + ", monthlyFee=" + monthlyFee + ", daysLate=" + daysLate + ", interestRate=" + interestRate
                + ", redraw=" + redraw + ", redrawBalance=" + redrawBalance + ", version=" + version + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Loan other = (Loan) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
