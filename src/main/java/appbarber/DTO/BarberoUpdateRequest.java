package appbarber.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BarberoUpdateRequest(
        @NotBlank String nombre,
        String fotoUrl,
        @NotNull Boolean activo
) {
}
