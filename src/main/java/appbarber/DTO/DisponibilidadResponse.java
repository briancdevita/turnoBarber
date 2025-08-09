package appbarber.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DisponibilidadResponse(
        Long empresaId,
        Long barberoId,
        Long servicioId,
        LocalDate dia,
        LocalTime ventanaDesde,
        LocalTime ventanaHasta,
        Integer stepMin,
        List<SlotDTO> slots
) {
}
