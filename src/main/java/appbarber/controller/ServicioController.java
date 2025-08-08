package appbarber.controller;


import appbarber.DTO.ServicioDTO;
import appbarber.repository.ServicioRepository;
import appbarber.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
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


}
