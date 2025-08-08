package appbarber.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "clientes",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_cliente_email_por_empresa", columnNames = {"empresa_id","email"})
        },
        indexes = {
                @Index(name="idx_clientes_empresa", columnList="empresa_id")
        })
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank
    @Column(length = 150, nullable = false)
    private String nombre;

    @Email
    @Column(length = 150)
    private String email;

    @Column(length = 50)
    private String telefono;

}
