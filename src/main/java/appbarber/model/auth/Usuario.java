package appbarber.model.auth;

import appbarber.enums.Rol;
import appbarber.model.base.Auditable;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity @Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name="uk_usuario_email", columnNames = "email")
})
public class Usuario extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email @NotBlank
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Rol rol = Rol.ADMIN;

    @Column(nullable = false)
    private boolean activo = true;
}
