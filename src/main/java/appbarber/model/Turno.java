package appbarber.model;

import appbarber.enums.EstadoTurno;
import appbarber.model.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"empresa","cliente","barbero","servicio"})
@Entity
@Table(name = "turnos",
        indexes = {
                @Index(name="idx_turnos_empresa", columnList="empresa_id"),
                @Index(name="idx_turnos_barbero_inicio", columnList="barbero_id,inicio"),
                @Index(name="idx_turnos_cliente_inicio", columnList="cliente_id,inicio")
        })
public class Turno extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime inicio;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fin;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoTurno estado = EstadoTurno.PENDIENTE;

    @Column(name = "precio_centavos")
    private Integer precioCentavos; // opcional (puede copiar de Servicio al reservar)

    @Column(columnDefinition = "TEXT")
    private String notas;
}
