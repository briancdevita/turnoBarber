package appbarber.service;


import appbarber.DTO.ServicioCreateRequest;
import appbarber.DTO.ServicioDTO;
import appbarber.DTO.ServicioMapper;
import appbarber.DTO.ServicioUpdateRequest;
import appbarber.exception.ConflictException;
import appbarber.exception.NotFoundException;
import appbarber.model.Empresa;
import appbarber.model.Servicio;
import appbarber.repository.EmpresaRepository;
import appbarber.repository.ServicioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;


    @Transactional()
    public List<ServicioDTO> listarPorEmpresa(Long empresaId, boolean soloActivos) {
        if (!empresaRepository.existsById(empresaId)) {
            throw new RuntimeException("Empresa no encontrada");
        }
        List<Servicio> servicios = soloActivos
                ? servicioRepository.findByEmpresaIdAndActivoTrueOrderByNombreAsc(empresaId)
                : servicioRepository.findByEmpresaIdOrderByNombreAsc(empresaId);

        return servicios.stream().map(ServicioMapper::toDTO).collect(Collectors.toList());

    }

    private Empresa requireEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
    }

    @Transactional
    public ServicioDTO crear(Long empresaId, ServicioCreateRequest req) {
        var empresa = requireEmpresa(empresaId);
        if (servicioRepository.existsByEmpresaIdAndNombreIgnoreCase(empresaId, req.nombre())) {
            throw new RuntimeException("Ya existe un servicio con el nombre: " + req.nombre());
        }
        var s = Servicio.builder()
                .empresa(empresa)
                .nombre(req.nombre().trim())
                .precioCentavos(req.precioCentavos())
                .duracionMinutos(req.duracionMinutos())
                .activo(Boolean.TRUE.equals(req.activo()))
                .build();
        return ServicioMapper.toDTO(servicioRepository.save(s));
    }



    @Transactional
    public ServicioDTO actualizar(Long empresaId, Long servicioId, ServicioUpdateRequest req) {
        requireEmpresa(empresaId);
        var servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        if (!servicio.getEmpresa().getId().equals(empresaId)) {
            throw new NotFoundException("El servicio no pertenece a la empresa especificada");
        }
        if (servicioRepository.existsByEmpresaIdAndNombreIgnoreCaseAndIdNot(empresaId, req.nombre(), servicioId)) {
            throw new ConflictException("Ya existe un servicio con el nombre: " + req.nombre());
        }
        servicio.setNombre(req.nombre().trim());
        servicio.setPrecioCentavos(req.precioCentavos());
        servicio.setDuracionMinutos(req.duracionMinutos());
        servicio.setActivo(req.activo());
        return ServicioMapper.toDTO(servicioRepository.save(servicio));
    }


    @Transactional
    public ServicioDTO cambiarEstado(Long empresaId, Long servicioId, boolean activo){
        requireEmpresa(empresaId);
        var servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));
        if (!servicio.getEmpresa().getId().equals(empresaId)) {
            throw new NotFoundException("El servicio no pertenece a la empresa especificada");
        }
        servicio.setActivo(activo);
        return ServicioMapper.toDTO(servicioRepository.save(servicio));
    }

    @Transactional
    public void eliminar(Long empresaId, Long servicioId) {
        requireEmpresa(empresaId);
        var servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));
        if (!servicio.getEmpresa().getId().equals(empresaId)) {
            throw new NotFoundException("El servicio no pertenece a la empresa especificada");
        }
        servicioRepository.delete(servicio); //Cambiar para que sea setActivo(false) en vez de eliminar
    }


}
