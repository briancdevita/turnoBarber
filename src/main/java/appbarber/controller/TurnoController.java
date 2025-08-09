package appbarber.controller;


import appbarber.DTO.TurnoCreateRequest;
import appbarber.DTO.TurnoDTO;
import appbarber.DTO.TurnoReprogramarRequest;
import appbarber.repository.TurnoRepository;
import appbarber.service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/empresas/{empresaId}")
public class TurnoController {



    @Autowired
    private TurnoService turnoService;


    @PostMapping("/turnos")
    @ResponseStatus(HttpStatus.CREATED)
    public TurnoDTO crear(
            @PathVariable Long empresaId,
            @Valid @RequestBody TurnoCreateRequest req
    ){
        return turnoService.crear(empresaId, req);
    }

    @GetMapping("/barberos/{barberoId}/turnos")
    public List<TurnoDTO> listarPorBarberoYDia(@PathVariable Long empresaId,
                                               @PathVariable Long barberoId,
                                               @RequestParam(required = false) String dia /* yyyy-MM-dd */) {
        var fecha = (dia == null) ? LocalDate.now() : LocalDate.parse(dia);
        var desde = fecha.atStartOfDay();
        var hasta = fecha.atTime(LocalTime.MAX);

        // Delegamos en repo via service simple (o podríamos tener un método en TurnoService si preferís)
        return turnoService.listarPorBarberoYRango(empresaId, barberoId, desde, hasta);
    }


    @PatchMapping("/turnos/{turnoId}/confirmar")
    public TurnoDTO confirmar(@PathVariable Long empresaId, @PathVariable Long turnoId) {
        return turnoService.confirmar(empresaId, turnoId);
    }

    @PatchMapping("/turnos/{turnoId}/cancelar")
    public TurnoDTO cancelar(@PathVariable Long empresaId, @PathVariable Long turnoId) {
        return turnoService.cancelar(empresaId, turnoId);
    }

    @PatchMapping("/turnos/{turnoId}/completar")
    public TurnoDTO completar(@PathVariable Long empresaId, @PathVariable Long turnoId) {
        return turnoService.completar(empresaId, turnoId);
    }

    @PatchMapping("/turnos/{turnoId}/reprogramar")
    public TurnoDTO reprogramar(@PathVariable Long empresaId, @PathVariable Long turnoId,
                                @Valid @RequestBody TurnoReprogramarRequest req) {
        return turnoService.reprogramar(empresaId, turnoId, req);
    }

    @GetMapping("/turnos")
    public List<TurnoDTO> listarEmpresa(@PathVariable Long empresaId,
                                        @RequestParam(required = false) String desde,
                                        @RequestParam(required = false) String hasta) {
        var d = (desde == null) ? LocalDate.now().atStartOfDay() : LocalDateTime.parse(desde);
        var h = (hasta == null) ? LocalDate.now().atTime(LocalTime.MAX) : LocalDateTime.parse(hasta);
        return turnoService.listarPorEmpresaYRango(empresaId, d, h);
    }

    @GetMapping("/clientes/{clienteId}/turnos")
    public List<TurnoDTO> listarCliente(@PathVariable Long empresaId, @PathVariable Long clienteId,
                                        @RequestParam(required = false) String desde,
                                        @RequestParam(required = false) String hasta) {
        var d = (desde == null) ? LocalDate.now().minusDays(30).atStartOfDay() : LocalDateTime.parse(desde);
        var h = (hasta == null) ? LocalDate.now().plusDays(30).atTime(LocalTime.MAX) : LocalDateTime.parse(hasta);
        return turnoService.listarPorClienteYRango(empresaId, clienteId, d, h);
    }


}
