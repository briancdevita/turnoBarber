package appbarber.service;


import appbarber.DTO.TurnoCreateRequest;
import appbarber.DTO.TurnoDTO;
import appbarber.DTO.TurnoMapper;
import appbarber.DTO.TurnoReprogramarRequest;
import appbarber.enums.EstadoTurno;
import appbarber.exception.ConflictException;
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

    private Turno requireTurnoEnEmpresa(Long empresaId, Long turnoId) {
        var t = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new NotFoundException("Turno no encontrado: " + turnoId));
        if (!t.getEmpresa().getId().equals(empresaId)) {
            throw new NotFoundException("El turno no pertenece a la empresa.");
        }
        return t;
    }

    @Transactional
    public TurnoDTO confirmar(Long empresaId, Long turnoId) {
        var t = requireTurnoEnEmpresa(empresaId, turnoId);
        if (t.getEstado() != EstadoTurno.PENDIENTE) {
            throw new ConflictException("Sólo se pueden confirmar turnos pendientes.");
        }
        t.setEstado(EstadoTurno.CONFIRMADO);
        return TurnoMapper.toDTO(t);
    }

    @Transactional
    public TurnoDTO cancelar(Long empresaId, Long turnoId) {
        var t = requireTurnoEnEmpresa(empresaId, turnoId);
        var limite = LocalDateTime.now().plusHours(2);
        if (t.getInicio().isBefore(limite)) {
            throw new ConflictException("La cancelación debe realizarse con al menos 2 horas de anticipación.");
        }
        t.setEstado(EstadoTurno.CANCELADO);
        return TurnoMapper.toDTO(t);
    }

    @Transactional
    public TurnoDTO completar(Long empresaId, Long turnoId) {
        var t = requireTurnoEnEmpresa(empresaId, turnoId);
        if (t.getEstado() == EstadoTurno.CANCELADO) {
            throw new ConflictException("No se puede completar un turno cancelado.");
        }
        if (t.getInicio().isAfter(LocalDateTime.now())) {
            throw new ConflictException("Sólo se pueden completar turnos ya iniciados.");
        }
        t.setEstado(EstadoTurno.COMPLETADO);
        return TurnoMapper.toDTO(t);
    }

    @Transactional
    public TurnoDTO reprogramar(Long empresaId, Long turnoId, TurnoReprogramarRequest req) {
        var t = requireTurnoEnEmpresa(empresaId, turnoId);
        if (t.getEstado() == EstadoTurno.CANCELADO) {
            throw new ConflictException("No se puede reprogramar un turno cancelado.");
        }
        var nuevoInicio = req.nuevoInicio();

        // regla 2h
        if (nuevoInicio.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("La nueva fecha/hora debe tener al menos 2 horas de anticipación.");
        }

        var nuevoFin = nuevoInicio.plusMinutes(t.getServicio().getDuracionMinutos());

        // no solape con otros turnos del mismo barbero
        if (turnoRepository.existsSolapado(empresaId, t.getBarbero().getId(), nuevoInicio, nuevoFin)) {
            throw new ConflictException("El barbero ya tiene un turno en el nuevo horario.");
        }

        t.setInicio(nuevoInicio);
        t.setFin(nuevoFin);
        // si estaba COMPLETADO lo impedimos; si querés permitir, quita este check
        if (t.getEstado() == EstadoTurno.COMPLETADO) {
            throw new ConflictException("No se puede reprogramar un turno completado.");
        }
        // opcional: al reprogramar, volvemos a PENDIENTE
        t.setEstado(EstadoTurno.PENDIENTE);

        return TurnoMapper.toDTO(t);
    }


    @Transactional()
    public List<TurnoDTO> listarPorEmpresaYRango(Long empresaId, LocalDateTime desde, LocalDateTime hasta) {
        return turnoRepository.findByEmpresaIdAndInicioBetweenOrderByInicioAsc(empresaId, desde, hasta)
                .stream().map(TurnoMapper::toDTO).toList();
    }

    @Transactional()
    public List<TurnoDTO> listarPorClienteYRango(Long empresaId, Long clienteId, LocalDateTime desde, LocalDateTime hasta) {
        return turnoRepository.findByEmpresaIdAndClienteIdAndInicioBetweenOrderByInicioAsc(empresaId, clienteId, desde, hasta)
                .stream().map(TurnoMapper::toDTO).toList();
    }



}
