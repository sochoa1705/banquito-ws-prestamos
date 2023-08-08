package ec.edu.espe.banquito.requirements.model;

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
@Table(name = "GUARANTOR")
public class Guarantor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GUARANTOR_ID", nullable = false)
    private Integer id;

    @Column(name = "CODE", length = 36, nullable = false)
    private String code;

    @Column(name = "TYPE", length = 3, nullable = false)
    private String type;

    @Column(name = "NAME", length = 30, nullable = false)
    private String name;

    @Column(name = "STATUS", length = 3)
    private String status;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;
}
