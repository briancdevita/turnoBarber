package appbarber.controller;


import appbarber.DTO.DisponibilidadResponse;
import appbarber.service.DisponibilidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Tag(name = "Slot Disponibilidad", description = "CRUD de disponibilidad por empresa")
@RestController
@RequestMapping("/api/empresas/{empresaId}/barberos/{barberoId}")
public class DisponibilidadController {


    @Autowired
    private DisponibilidadService disponibilidadService;


    @Operation(summary = "Obtener disponibilidad de un barbero para un servicio en una fecha específica")
    @GetMapping("/disponibilidad")
    public DisponibilidadResponse disponibilidad(
            @PathVariable Long empresaId,
            @PathVariable Long barberoId,
            @RequestParam Long servicioId,
            @RequestParam String dia,                // yyyy-MM-dd
            @RequestParam(required = false) String desde, // HH:mm
            @RequestParam(required = false) String hasta, // HH:mm
            @RequestParam(required = false, defaultValue = "15") Integer stepMin
    ) {
        var date = LocalDate.parse(dia);
        var from = (desde == null) ? null : LocalTime.parse(desde);
        var to   = (hasta == null) ? null : LocalTime.parse(hasta);
        return disponibilidadService.calcular(empresaId, barberoId, servicioId, date, from, to, stepMin);
    }
}
