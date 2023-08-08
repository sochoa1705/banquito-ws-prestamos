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
@Table(name = "ASSET")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASSET_ID", nullable = false)
    private Integer id;

    @Column(name = "AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "GUARANTOR_CODE", length = 36, nullable = false)
    private String guarantorCode;

    @Column(name = "GUARANTOR_TYPE", length = 3, nullable = false)
    private String guarantorType;

    @Column(name = "CURRENCY", length = 3)
    private String currency;

    @Column(name = "STATUS", length = 3)
    private String status;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;
}
