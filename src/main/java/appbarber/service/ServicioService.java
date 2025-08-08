package appbarber.service;


import appbarber.DTO.ServicioDTO;
import appbarber.DTO.ServicioMapper;
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
}
