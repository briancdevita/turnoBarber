package appbarber.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record MagicLinkRequest(
        @NotNull Long empresaId, @Email String emailCliente
) {
}
