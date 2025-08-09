package appbarber.DTO;

import jakarta.validation.constraints.NotNull;

public record TurnoEtadoRequest(
        @NotNull Boolean confirmar
) {
}
