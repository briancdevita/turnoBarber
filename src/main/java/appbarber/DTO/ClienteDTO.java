package appbarber.DTO;

public record ClienteDTO(
        Long id,
        Long empresaId,
        String nombre,
        String email,
        String telefono
) {
}
