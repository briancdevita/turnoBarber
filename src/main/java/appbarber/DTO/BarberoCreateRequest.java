package appbarber.DTO;

import jakarta.validation.constraints.NotBlank;

public record BarberoCreateRequest(
        @NotBlank String nombre,
        String fotoUrl,
        Boolean activo
) {
}
