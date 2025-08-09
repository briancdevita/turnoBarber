package appbarber.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;



@Schema(description = "Solicitud para crear un turno")
public record TurnoCreateRequest(
       @Schema(example = "1") @NotNull Long clienteId,
       @Schema(example = "1") @NotNull Long barberoId,
       @Schema(example = "1") @NotNull Long servicioId,
       @Schema(example = "2025-08-12T15:00:00")  @NotNull LocalDateTime inicio,
       @Schema(example = "70000", description = "Precio opcional en centavos; por defecto se copia del servicio") Integer precioCentavos,
       @Schema(example = "Traer imagen de referencia") String notas
        ) {
}
