package appbarber.DTO;

import appbarber.model.Barbero;

public class BarberoMapper {
    public static BarberoDTO toDTO(Barbero b) {
        return new BarberoDTO(b.getId(), b.getEmpresa().getId(), b.getNombre(), b.getFotoUrl(), b.isActivo());
    }
}
