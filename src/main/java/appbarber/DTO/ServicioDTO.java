package appbarber.DTO;

public record ServicioDTO(
        Long id,
        String nombre,
        Integer precioCentavos,
        Integer duracionMinutos,
        Boolean activo
) {
}
