package appbarber.config;

import appbarber.model.*;
import appbarber.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("dev")
public class DevDataLoader implements CommandLineRunner {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private BarberoRepository barberoRepository;

    @Autowired
    ServicioRepository servicioRepository;

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    private TurnoRepository turnoRepository;


    @Override
    public void run(String... args) throws Exception {
        if (empresaRepository.count() > 0) return;

        var empresa = empresaRepository.save(
                Empresa.builder()
                        .nombre("Barbería Central")
                        .direccion("Calle Falsa 123")
                        .emailAdmin("admin@central.com")
                        .telefono("+54-11-5555-0000")
                        .build());

        var b1 = barberoRepository.save(Barbero.builder().empresa(empresa).nombre("Matías").activo(true).build());
        var b2 = barberoRepository.save(Barbero.builder().empresa(empresa).nombre("Luz").activo(true).build());

        servicioRepository.save(Servicio.builder().empresa(empresa).nombre("Corte clásico")
                .precioCentavos(70000).duracionMinutos(30).activo(true).build());
        servicioRepository.save(Servicio.builder().empresa(empresa).nombre("Afeitado")
                .precioCentavos(50000).duracionMinutos(20).activo(true).build());
        servicioRepository.save(Servicio.builder().empresa(empresa).nombre("Perfilado de barba")
                .precioCentavos(45000).duracionMinutos(15).activo(false).build());



        clienteRepository.save(Cliente.builder().empresa(empresa).nombre("Juan Pérez").email("juan@mail.com").telefono("111-222").build());
    }
}
