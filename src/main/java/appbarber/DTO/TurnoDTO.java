package appbarber.DTO;

import appbarber.enums.EstadoTurno;

import java.time.LocalDateTime;

public record TurnoDTO (
        Long id,
        Long empresaId,
        Long clienteId,
        Long barberoId,
        Long servicioId,
        LocalDateTime inicio,
        LocalDateTime fin,
        EstadoTurno estado,
        Integer precioCentavos,
        String notas
) {

}
