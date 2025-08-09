package appbarber.service;


import appbarber.DTO.BarberoCreateRequest;
import appbarber.DTO.BarberoDTO;
import appbarber.DTO.BarberoMapper;
import appbarber.DTO.BarberoUpdateRequest;
import appbarber.exception.ConflictException;
import appbarber.exception.NotFoundException;
import appbarber.model.Barbero;
import appbarber.model.Empresa;
import appbarber.repository.BarberoRepository;
import appbarber.repository.EmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BarberoService {

    @Autowired
    private  BarberoRepository barberoRepository;

    @Autowired
    private  EmpresaRepository empresaRepository;


    private Empresa requireEmpresa(Long empresaId) {
        return empresaRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

    }


    @Transactional
    public BarberoDTO crear(Long empresaId, BarberoCreateRequest req) {
        var empresa = requireEmpresa(empresaId);
        if (barberoRepository.existsByEmpresaIdAndNombreIgnoreCase(empresaId, req.nombre())) {
            throw new ConflictException("Ya existe un barbero con ese nombre en la empresa.");
        }
        var b = Barbero.builder()
                .empresa(empresa)
                .nombre(req.nombre().trim())
                .fotoUrl(req.fotoUrl())
                .activo(req.activo() == null ? true : req.activo())
                .build();
        return BarberoMapper.toDTO(barberoRepository.save(b));
    }

    @Transactional
    public BarberoDTO actualizar(Long empresaId, Long barberoId, BarberoUpdateRequest req) {
        requireEmpresa(empresaId);
        var b = barberoRepository.findById(barberoId)
                .orElseThrow(() -> new NotFoundException("Barbero no encontrado: " + barberoId));
        if (!b.getEmpresa().getId().equals(empresaId)) throw new NotFoundException("Barbero no pertenece a la empresa.");
        if (barberoRepository.existsByEmpresaIdAndNombreIgnoreCaseAndIdNot(empresaId, req.nombre(), barberoId)) {
            throw new ConflictException("Ya existe otro barbero con ese nombre en la empresa.");
        }
        b.setNombre(req.nombre().trim());
        b.setFotoUrl(req.fotoUrl());
        b.setActivo(req.activo());
        return BarberoMapper.toDTO(b);
    }

    @Transactional
    public void eliminar(Long empresaId, Long barberoId) {
        requireEmpresa(empresaId);
        var b = barberoRepository.findById(barberoId)
                .orElseThrow(() -> new NotFoundException("Barbero no encontrado: " + barberoId));
        if (!b.getEmpresa().getId().equals(empresaId)) throw new NotFoundException("Barbero no pertenece a la empresa.");
        barberoRepository.delete(b);
    }


    @Transactional()
    public Page<BarberoDTO> buscar(Long empresaId, String q, Pageable pageable) {
        requireEmpresa(empresaId);
        var page = barberoRepository.findByEmpresaIdAndNombreContainingIgnoreCase(empresaId, q == null ? "" : q, pageable);
        return page.map(BarberoMapper::toDTO);
    }


}
