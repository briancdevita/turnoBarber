package appbarber.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TurnoReprogramarRequest(
        @NotNull LocalDateTime nuevoInicio
        ) {
}
