package appbarber.controller;


import appbarber.DTO.ServicioCreateRequest;
import appbarber.DTO.ServicioDTO;
import appbarber.DTO.ServicioEstadoRequest;
import appbarber.DTO.ServicioUpdateRequest;
import appbarber.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Servicios", description = "CRUD de servicios por empresa")
@RestController
@RequestMapping("/api/empresas/{empresaId}/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;


    @Operation(summary = "Listar servicios", description = "Permite filtrar por activos y devuelve DTOs ordenados por nombre")
    @GetMapping
    public List<ServicioDTO> listar(
            @PathVariable Long empresaId,
            @RequestParam(name = "activos", defaultValue = "false") boolean activos) {
     {
         System.out.println("Id de empresa: " + empresaId);
        return servicioService.listarPorEmpresa(empresaId, activos);
    }

    }

    @Operation(summary = "Crear servicio")
    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ServicioDTO crear(
            @PathVariable Long empresaId,
            @Valid
            @RequestBody ServicioCreateRequest req
    ){
        return servicioService.crear(empresaId, req);
    }

    @Operation(summary = "Actualizar servicio")
    @PutMapping("/{servicioId}")
    public ServicioDTO actualizar(
            @PathVariable Long empresaId,
            @PathVariable Long servicioId,
            @Valid
            @RequestBody ServicioUpdateRequest req
    ){
    return servicioService.actualizar(empresaId, servicioId, req);
    }

    @Operation(summary = "Cambiar estado de servicio")
    @PatchMapping("/{servicioId}/estado")
    public ServicioDTO cambiarEstado(@PathVariable Long empresaId, @PathVariable Long servicioId,
                                     @Valid @RequestBody ServicioEstadoRequest req) {
        return servicioService.cambiarEstado(empresaId, servicioId, req.activo());
    }

    @Operation(summary = "Eliminar servicio")
    @DeleteMapping("/{servicioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long empresaId, @PathVariable Long servicioId) {
        servicioService.eliminar(empresaId, servicioId);
    }


}
