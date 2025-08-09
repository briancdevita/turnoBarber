package appbarber.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteUpdateRequest (
        @NotBlank String nombre,
        @Email String email,
        String telefono
){
}
