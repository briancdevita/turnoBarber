package appbarber.controller;


import appbarber.DTO.ServicioCreateRequest;
import appbarber.DTO.ServicioDTO;
import appbarber.DTO.ServicioEstadoRequest;
import appbarber.DTO.ServicioUpdateRequest;
import appbarber.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas/{empresaId}/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public List<ServicioDTO> listar(
            @PathVariable Long empresaId,
            @RequestParam(name = "activos", defaultValue = "false") boolean activos) {
     {
         System.out.println("Id de empresa: " + empresaId);
        return servicioService.listarPorEmpresa(empresaId, activos);
    }

    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ServicioDTO crear(
            @PathVariable Long empresaId,
            @Valid
            @RequestBody ServicioCreateRequest req
    ){
        return servicioService.crear(empresaId, req);
    }


    @PutMapping("/{servicioId}")
    public ServicioDTO actualizar(
            @PathVariable Long empresaId,
            @PathVariable Long servicioId,
            @Valid
            @RequestBody ServicioUpdateRequest req
    ){
    return servicioService.actualizar(empresaId, servicioId, req);
    }

    @PatchMapping("/{servicioId}/estado")
    public ServicioDTO cambiarEstado(@PathVariable Long empresaId, @PathVariable Long servicioId,
                                     @Valid @RequestBody ServicioEstadoRequest req) {
        return servicioService.cambiarEstado(empresaId, servicioId, req.activo());
    }


    @DeleteMapping("/{servicioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long empresaId, @PathVariable Long servicioId) {
        servicioService.eliminar(empresaId, servicioId);
    }


}
