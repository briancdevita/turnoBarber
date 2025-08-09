package appbarber.controller;


import appbarber.DTO.MagicLinkRequest;
import appbarber.DTO.MagicLinkResponse;
import appbarber.DTO.TurnoDTO;
import appbarber.service.MagicLinkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/magic")
public class MagicLinkController {

    private final MagicLinkService service;
    public MagicLinkController(MagicLinkService service) { this.service = service; }

    @PostMapping("/link")
    @ResponseStatus(HttpStatus.CREATED)
    public MagicLinkResponse solicitar(@Valid @RequestBody MagicLinkRequest req) {
        return service.solicitar(req);
    }

    @GetMapping("/{token}/turnos")
    public List<TurnoDTO> turnos(@PathVariable String token) {
        return service.turnos(token);
    }

    @DeleteMapping("/{token}/turnos/{turnoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable String token, @PathVariable Long turnoId) {
        service.cancelar(token, turnoId);
    }


}
