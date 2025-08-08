package appbarber.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "empresas",
        indexes = { @Index(name="idx_empresas_nombre", columnList="nombre") })
public class Empresa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 150, nullable = false)
    private String nombre;

    @Column(length = 200)
    private String direccion;

    @Email
    @Column(name = "email_admin", length = 150)
    private String emailAdmin;

    @Column(length = 50)
    private String telefono;


}
