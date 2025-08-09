package appbarber.DTO;

public record BarberoDTO(
        Long id,
        Long empresaId,
        String nombre,
        String fotoUrl,
        Boolean activo
) {
}
