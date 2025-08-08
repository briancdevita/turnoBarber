package appbarber.DTO;

import appbarber.model.Servicio;

public class ServicioMapper {
    public static ServicioDTO toDTO(Servicio s) {
        return new ServicioDTO(
            s.getId(),
            s.getNombre(),
            s.getPrecioCentavos(),
            s.getDuracionMinutos(),
            s.isActivo()
        );
    }
}
