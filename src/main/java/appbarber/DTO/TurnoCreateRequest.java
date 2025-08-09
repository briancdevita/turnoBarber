package appbarber.DTO;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TurnoCreateRequest(
        @NotNull Long clienteId,
        @NotNull Long barberoId,
        @NotNull Long servicioId,
        @NotNull LocalDateTime inicio,
        Integer precioCentavos,
        String notas
        ) {
}
