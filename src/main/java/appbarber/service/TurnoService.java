package appbarber.service;


import appbarber.DTO.TurnoCreateRequest;
import appbarber.DTO.TurnoDTO;
import appbarber.DTO.TurnoMapper;
import appbarber.enums.EstadoTurno;
import appbarber.exception.NotFoundException;
import appbarber.model.Empresa;
import appbarber.model.Turno;
import appbarber.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private BarberoRepository barberoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private Empresa requireEmpresa(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + id));
    }

    @Transactional
    public TurnoDTO crear (Long empresaId, TurnoCreateRequest req) {
        var empresa = requireEmpresa(empresaId);

       var cliente = clienteRepository.findById(req.clienteId())
               .orElseThrow(()-> new NotFoundException("Cliente no encontrada con id: " + req.clienteId()));
       if (!cliente.getEmpresa().getId().equals(empresaId)) {
              throw new NotFoundException("Cliente no pertenece a la empresa con id: " + empresaId);
       }


       var barbero = barberoRepository.findById(req.barberoId())
                .orElseThrow(()-> new NotFoundException("Barbero no encontrado con id: " + req.barberoId()));
         if (!barbero.getEmpresa().getId().equals(empresaId)) {
            throw new NotFoundException("Barbero no pertenece a la empresa con id: " + empresaId);
         }
       var servicio = servicioRepository.findById(req.servicioId())
                .orElseThrow(()-> new NotFoundException("Servicio no encontrado con id: " + req.servicioId()));
        if (!servicio.getEmpresa().getId().equals(empresaId)) {
            throw new NotFoundException("Servicio no pertenece a la empresa con id: " + empresaId);
        }

       var hora = LocalDateTime.now().plusHours(2);
        if (req.inicio().isBefore(hora)) {
            throw new RuntimeException("El inicio debe ser al menos 2 horas desde ahora.");
        }

       var fin = req.inicio().plusMinutes(servicio.getDuracionMinutos());

        if (turnoRepository.existsSolapado(empresaId, req.barberoId(), req.inicio(), fin)) {
            throw new RuntimeException("El barbero ya tiene un turno en ese horario.");
        }

        var turno = Turno.builder()
                .empresa(empresa)
                .cliente(cliente)
                .barbero(barbero)
                .servicio(servicio)
                .inicio(req.inicio())
                .fin(fin)
                .estado(EstadoTurno.PENDIENTE)
                .precioCentavos(req.precioCentavos() != null ? req.precioCentavos() : servicio.getPrecioCentavos())
                .notas(req.notas())
                .build();

        return TurnoMapper.toDTO(turnoRepository.save(turno));


    }


    @Transactional()
    public List<TurnoDTO> listarPorBarberoYRango(Long empresaId, Long barberoId,
                                                 LocalDateTime desde, LocalDateTime hasta) {
        // no hace falta cargar entidades — validamos existencia mínima si querés
        var turnos = turnoRepository.findByEmpresaIdAndBarberoIdAndInicioBetweenOrderByInicioAsc(
                empresaId, barberoId, desde, hasta);
        return turnos.stream().map(TurnoMapper::toDTO).toList();
    }



}
