package appbarber.model.auth;




import appbarber.model.Cliente;
import appbarber.model.Empresa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "magic_link_tokens", indexes = {
        @Index(name="idx_magic_token", columnList = "token")
})
public class MagicLinkToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(optional = false) @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiraEn;

    private LocalDateTime usadoEn;
    private boolean invalidado = false;

    public boolean isVigente() {
        return !invalidado && usadoEn == null && LocalDateTime.now().isBefore(expiraEn);
    }
}
