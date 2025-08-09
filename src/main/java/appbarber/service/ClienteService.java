package appbarber.service;


import appbarber.DTO.ClienteCreateRequest;
import appbarber.DTO.ClienteDTO;
import appbarber.DTO.ClienteMapper;
import appbarber.DTO.ClienteUpdateRequest;
import appbarber.exception.ConflictException;
import appbarber.exception.NotFoundException;
import appbarber.model.Cliente;
import appbarber.model.Empresa;
import appbarber.repository.ClienteRepository;
import appbarber.repository.EmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;


    private Empresa requireEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada: " + empresaId));
    }



    @Transactional
    public ClienteDTO crear(Long empresaId, ClienteCreateRequest req) {
        var empresa = requireEmpresa(empresaId);
        if (req.email() != null && clienteRepository.existsByEmpresaIdAndEmailIgnoreCase(empresaId, req.email())) {
            throw new ConflictException("Ya existe un cliente con ese email en la empresa.");
        }
        var c = Cliente.builder()
                .empresa(empresa)
                .nombre(req.nombre().trim())
                .email(req.email())
                .telefono(req.telefono())
                .build();
        return ClienteMapper.toDTO(clienteRepository.save(c));
    }

    @Transactional
    public ClienteDTO actualizar(Long empresaId, Long clienteId, ClienteUpdateRequest req) {
        requireEmpresa(empresaId);
        var c = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + clienteId));
        if (!c.getEmpresa().getId().equals(empresaId))
            throw new NotFoundException("Cliente no pertenece a la empresa.");
        if (req.email() != null && !req.email().equalsIgnoreCase(c.getEmail())
            && clienteRepository.existsByEmpresaIdAndEmailIgnoreCase(empresaId, req.email())) {
            throw new ConflictException("Ya existe otro cliente con ese email en la empresa.");
        }
        c.setNombre(req.nombre().trim());
        c.setEmail(req.email());
        c.setTelefono(req.telefono());
        return ClienteMapper.toDTO(c);


    }

    @Transactional
    public void eliminar(Long empresaId, Long clienteId) {
        requireEmpresa(empresaId);
        var c = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + clienteId));
        if (!c.getEmpresa().getId().equals(empresaId)) throw new NotFoundException("Cliente no pertenece a la empresa.");
        clienteRepository.delete(c);
    }

    @Transactional()
    public Page<ClienteDTO> buscar(Long empresaId, String qNombre, String qEmail, String qTel, Pageable pageable) {
        requireEmpresa(empresaId);
        if (qEmail != null && !qEmail.isBlank()) {
            return clienteRepository.findByEmpresaIdAndEmailContainingIgnoreCase(empresaId, qEmail, pageable).map(ClienteMapper::toDTO);
        }
        if (qTel != null && !qTel.isBlank()) {
            return clienteRepository.findByEmpresaIdAndTelefonoContainingIgnoreCase(empresaId, qTel, pageable).map(ClienteMapper::toDTO);
        }
        return clienteRepository.findByEmpresaIdAndNombreContainingIgnoreCase(empresaId, qNombre == null ? "" : qNombre, pageable)
                .map(ClienteMapper::toDTO);
    }


}
