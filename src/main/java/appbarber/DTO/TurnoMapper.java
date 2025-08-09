package appbarber.DTO;

import appbarber.model.Turno;

public class TurnoMapper {
    public static TurnoDTO toDTO(Turno t) {
        return new TurnoDTO(
                t.getId(),
                t.getEmpresa().getId(),
                t.getCliente().getId(),
                t.getBarbero().getId(),
                t.getServicio().getId(),
                t.getInicio(),
                t.getFin(),
                t.getEstado(),
                t.getPrecioCentavos(),
                t.getNotas()
        );
    }
}
