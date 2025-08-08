package appbarber.model;

import appbarber.model.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "empresa")
@Entity
@Table(name = "servicios",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_servicio_nombre_por_empresa", columnNames = {"empresa_id","nombre"})
        },
        indexes = { @Index(name="idx_servicios_empresa", columnList="empresa_id") })
public class Servicio  extends Auditable {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank
    @Column(length = 120, nullable = false)
    private String nombre;

    // precios en centavos para evitar decimales
    @Positive
    @Column(name = "precio_centavos", nullable = false)
    private Integer precioCentavos;

    @Positive
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false)
    private boolean activo = true;
}

