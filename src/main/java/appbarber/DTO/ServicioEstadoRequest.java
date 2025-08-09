package appbarber.DTO;

import jakarta.validation.constraints.NotNull;

public record ServicioEstadoRequest(
        @NotNull
        Boolean activo
) {
}
