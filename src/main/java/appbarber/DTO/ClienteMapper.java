package appbarber.DTO;

import appbarber.model.Cliente;

public class ClienteMapper {
    public static ClienteDTO toDTO(Cliente c) {
        return new ClienteDTO(c.getId(), c.getEmpresa().getId(), c.getNombre(), c.getEmail(), c.getTelefono());
    }
}
