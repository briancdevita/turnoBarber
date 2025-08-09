package appbarber.controller;


import appbarber.DTO.TurnoCreateRequest;
import appbarber.DTO.TurnoDTO;
import appbarber.repository.TurnoRepository;
import appbarber.service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

}
