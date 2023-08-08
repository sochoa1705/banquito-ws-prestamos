package ec.edu.espe.banquito.requirements.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "INTEREST_ACCRUE")
public class InterestAccrue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INTEREST_ACCRUE_ID", nullable = false)
    private Integer id;

    @Column(name = "INTEREST_RATE", precision = 18, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(name = "INTEREST_TYPE", length = 10, nullable = false)
    private String interestType;

    @Column(name = "SPREAD", precision = 18, scale = 2)
    private BigDecimal spread;

    @Column(name = "CHARGE_FRECUENCY", length = 10)
    private String chargeFrecuency;

    @Column(name = "STATUS", length = 3)
    private String status;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;
}
