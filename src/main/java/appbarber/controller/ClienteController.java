package appbarber.controller;


import appbarber.DTO.ClienteCreateRequest;
import appbarber.DTO.ClienteDTO;
import appbarber.DTO.ClienteUpdateRequest;
import appbarber.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresas/{empresaId}/clientes")
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public Page<ClienteDTO> listar(@PathVariable Long empresaId,
                                   @RequestParam(required = false) String qNombre,
                                   @RequestParam(required = false) String qEmail,
                                   @RequestParam(required = false) String qTel,
                                   Pageable pageable) {
        return clienteService.buscar(empresaId, qNombre, qEmail, qTel, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO crear(@PathVariable Long empresaId, @Valid @RequestBody ClienteCreateRequest req) {
        return clienteService.crear(empresaId, req);
    }

    @PutMapping("/{clienteId}")
    public ClienteDTO actualizar(@PathVariable Long empresaId, @PathVariable Long clienteId,
                                 @Valid @RequestBody ClienteUpdateRequest req) {
        return clienteService.actualizar(empresaId, clienteId, req);
    }

    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long empresaId, @PathVariable Long clienteId) {
        clienteService.eliminar(empresaId, clienteId);
    }
}
