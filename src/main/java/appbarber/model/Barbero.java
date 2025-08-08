package appbarber.model;


import appbarber.model.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "empresa")
@Entity
@Table(name = "barberos",
        indexes = { @Index(name="idx_barberos_empresa", columnList="empresa_id") })
public class Barbero extends Auditable {



    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank
    @Column(length = 150, nullable = false)
    private String nombre;

    @Column(name = "foto_url", length = 300)
    private String fotoUrl;

    @Column(nullable = false)
    private boolean activo = true;

}
