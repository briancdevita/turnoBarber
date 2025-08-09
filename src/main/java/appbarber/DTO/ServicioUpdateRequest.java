package appbarber.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServicioUpdateRequest(
        @NotBlank String nombre,
        @NotNull @Min(1) Integer precioCentavos,
        @NotNull @Min(1) Integer duracionMinutos,
        @NotNull Boolean activo
) {
}
