package appbarber.enums;

import java.time.OffsetDateTime;

public record ApiError(
        String path,
        int status,
        String message,
        OffsetDateTime timestamp
) {
}
