package appbarber.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteCreateRequest(
        @NotBlank String nombre,
        @Email String email,
        String telefono
) {
}
