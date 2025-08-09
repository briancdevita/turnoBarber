package appbarber.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SlotDTO(
        LocalDateTime inicio,
        LocalDateTime fin
) {
}
