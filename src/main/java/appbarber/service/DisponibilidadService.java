package appbarber.service;


import appbarber.DTO.DisponibilidadResponse;
import appbarber.DTO.SlotDTO;
import appbarber.enums.EstadoTurno;
import appbarber.exception.ConflictException;
import appbarber.exception.NotFoundException;
import appbarber.repository.BarberoRepository;
import appbarber.repository.EmpresaRepository;
import appbarber.repository.ServicioRepository;
import appbarber.repository.TurnoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DisponibilidadService {

    record Intervalo(LocalDateTime i, LocalDateTime f) {}

    @Autowired
    private EmpresaRepository empresaRepo;

    @Autowired
    private BarberoRepository barberoRepo;

    @Autowired
    private ServicioRepository servicioRepo;

    @Autowired
    private TurnoRepository turnoRepo;


    @Transactional()
    public DisponibilidadResponse calcular(
            Long empresaId, Long barberoId, Long servicioId,
            LocalDate dia, LocalTime desde, LocalTime hasta, int stepMin
    ) {
        // Validaciones de pertenencia/estado
        var empresa = empresaRepo.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada: " + empresaId));

        var barbero = barberoRepo.findById(barberoId)
                .orElseThrow(() -> new NotFoundException("Barbero no encontrado: " + barberoId));
        if (!barbero.getEmpresa().getId().equals(empresaId) || !barbero.isActivo()) {
            throw new ConflictException("Barbero no pertenece a la empresa o no está activo.");
        }

        var servicio = servicioRepo.findById(servicioId)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado: " + servicioId));
        if (!servicio.getEmpresa().getId().equals(empresaId) || !servicio.isActivo()) {
            throw new ConflictException("Servicio no pertenece a la empresa o no está activo.");
        }

        var ventanaDesde = (desde == null) ? LocalTime.of(10, 0) : desde;
        var ventanaHasta = (hasta == null) ? LocalTime.of(20, 0) : hasta;
        var duracion = Duration.ofMinutes(servicio.getDuracionMinutos());

        var inicioVentana = dia.atTime(ventanaDesde);
        var finVentana = dia.atTime(ventanaHasta);

        if (!finVentana.isAfter(inicioVentana)) {
            throw new ConflictException("La ventana horaria es inválida.");
        }
        if (stepMin <= 0 || (servicio.getDuracionMinutos() % stepMin) != 0) {
            // no obligatoria la divisibilidad, pero ayuda a alinear slots
            // Si querés permitir cualquier step, quitá esta validación
            throw new ConflictException("stepMin debe ser > 0 y divisor de la duración del servicio.");
        }

        // Cargar turnos del barbero en el día
        var turnos = turnoRepo.findByEmpresaIdAndBarberoIdAndInicioBetweenOrderByInicioAsc(
                empresaId, barberoId, inicioVentana.minusDays(0), finVentana.plusDays(0)
        );

        // Construir lista de intervalos ocupados (excluyendo CANCELADO)



        List<Intervalo> ocupados = new ArrayList<>();
        turnos.stream()
                .filter(t -> t.getEstado() != EstadoTurno.CANCELADO)
                .forEach(t -> ocupados.add(new Intervalo(t.getInicio(), t.getFin())));
        ocupados.sort(Comparator.comparing(o -> o.i));

        // Normalizar: recortar ocupados a la ventana de trabajo
        List<Intervalo> busy = new ArrayList<>();
        for (var o : ocupados) {
            var i = o.i.isBefore(inicioVentana) ? inicioVentana : o.i;
            var f = o.f.isAfter(finVentana) ? finVentana : o.f;
            if (f.isAfter(i)) busy.add(new Intervalo(i, f));
        }

        // Unir solapes en busy
        List<Intervalo> merged = new ArrayList<>();
        for (var b : busy) {
            if (merged.isEmpty() || !overlapOrTouch(merged.get(merged.size()-1), b)) {
                merged.add(b);
            } else {
                var last = merged.remove(merged.size()-1);
                merged.add(new Intervalo(last.i, max(last.f, b.f)));
            }
        }

        // Generar huecos libres dentro de [inicioVentana, finVentana]
        List<Intervalo> libres = new ArrayList<>();
        LocalDateTime cursor = inicioVentana;
        for (var b : merged) {
            if (b.i.isAfter(cursor)) libres.add(new Intervalo(cursor, b.i));
            cursor = max(cursor, b.f);
        }
        if (cursor.isBefore(finVentana)) libres.add(new Intervalo(cursor, finVentana));

        // Generar slots por stepMin donde entre la duración completa + regla 2h
        LocalDateTime limiteMinReserva = LocalDateTime.now().plusHours(2);
        List<SlotDTO> slots = new ArrayList<>();
        for (var gap : libres) {
            var start = alignUp(gap.i, stepMin);
            while (!start.plus(duracion).isAfter(gap.f)) {
                if (!start.isBefore(limiteMinReserva)) {
                    slots.add(new SlotDTO(start, start.plus(duracion)));
                }
                start = start.plusMinutes(stepMin);
            }
        }

        return new DisponibilidadResponse(
                empresa.getId(), barbero.getId(), servicio.getId(),
                dia, ventanaDesde, ventanaHasta, stepMin, slots
        );
    }

    private static boolean overlapOrTouch(Intervalo a, Intervalo b) {
        return !a.f.isBefore(b.i) && !b.f.isBefore(a.i);
    }

    private static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.isAfter(b) ? a : b;
    }

    /** Alinea hacia arriba al múltiplo de stepMin (p. ej. 10:07 → 10:15 si step=15). */
    private static LocalDateTime alignUp(LocalDateTime t, int stepMin) {
        int minutes = t.getMinute();
        int mod = minutes % stepMin;
        if (mod == 0) return t.withSecond(0).withNano(0);
        int add = stepMin - mod;
        return t.plusMinutes(add).withSecond(0).withNano(0);
    }

}
