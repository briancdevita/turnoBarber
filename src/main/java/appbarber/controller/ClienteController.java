package appbarber.controller;


import appbarber.DTO.ClienteCreateRequest;
import appbarber.DTO.ClienteDTO;
import appbarber.DTO.ClienteUpdateRequest;
import appbarber.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Clientes", description = "CRUD de clientes por empresa")
@RestController
@RequestMapping("/api/empresas/{empresaId}/clientes")
public class ClienteController {


    @Autowired
    private ClienteService clienteService;



    @Operation(summary = "Listar clientes de una empresa")
    @GetMapping
    public Page<ClienteDTO> listar(@PathVariable Long empresaId,
                                   @RequestParam(required = false) String qNombre,
                                   @RequestParam(required = false) String qEmail,
                                   @RequestParam(required = false) String qTel,
                                   Pageable pageable) {
        return clienteService.buscar(empresaId, qNombre, qEmail, qTel, pageable);
    }

    @Operation(summary = "Crear un nuevo cliente")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO crear(@PathVariable Long empresaId, @Valid @RequestBody ClienteCreateRequest req) {
        return clienteService.crear(empresaId, req);
    }

    @Operation(summary = "Actualizar un cliente por ID")
    @PutMapping("/{clienteId}")
    public ClienteDTO actualizar(@PathVariable Long empresaId, @PathVariable Long clienteId,
                                 @Valid @RequestBody ClienteUpdateRequest req) {
        return clienteService.actualizar(empresaId, clienteId, req);
    }

    @Operation(summary = "Eliminar un cliente por ID")
    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long empresaId, @PathVariable Long clienteId) {
        clienteService.eliminar(empresaId, clienteId);
    }
}
