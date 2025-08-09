package appbarber.controller;


import appbarber.DTO.BarberoCreateRequest;
import appbarber.DTO.BarberoDTO;
import appbarber.DTO.BarberoUpdateRequest;
import appbarber.service.BarberoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Barberos", description = "CRUD de barberos por empresa")
@RestController
@RequestMapping("/api/empresas/{empresaId}/barberos")
public class BarberoController {

    @Autowired
    private BarberoService barberoService;


    @Operation(summary = "Listar barberos de una empresa")
    @GetMapping
    public Page<BarberoDTO> listar(@PathVariable Long empresaId,
                                   @RequestParam(required = false) String q,
                                   Pageable pageable) {
        return barberoService.buscar(empresaId, q, pageable);
    }


    @Operation(summary = "Crear un nuevo barbero")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BarberoDTO crear(@PathVariable Long empresaId, @Valid @RequestBody BarberoCreateRequest req) {
        return barberoService.crear(empresaId, req);
    }

    @Operation(summary = "Actualizar un barbero por ID")
    @PutMapping("/{barberoId}")
    public BarberoDTO actualizar(@PathVariable Long empresaId, @PathVariable Long barberoId,
                                 @Valid @RequestBody BarberoUpdateRequest req) {
        return barberoService.actualizar(empresaId, barberoId, req);
    }

    @Operation(summary = "Eliminar un barbero por ID")
    @DeleteMapping("/{barberoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long empresaId, @PathVariable Long barberoId) {
        barberoService.eliminar(empresaId, barberoId);
    }



}
