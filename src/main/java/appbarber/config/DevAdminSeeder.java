package appbarber.config;


import appbarber.enums.Rol;
import appbarber.model.auth.Usuario;
import appbarber.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@Profile("dev")
public class DevAdminSeeder implements CommandLineRunner {
    private final UsuarioRepository repo;


    private final PasswordEncoder encoder;

    public DevAdminSeeder(UsuarioRepository repo, PasswordEncoder encoder) { this.repo = repo; this.encoder = encoder; }

    @Override public void run(String... args) {
        if (repo.count() == 0) {
            repo.save(Usuario.builder()
                    .email("admin@barberia.local")
                    .passwordHash(encoder.encode("admin123")) // cambia en prod
                    .rol(Rol.ADMIN)
                    .activo(true)
                    .build());
        }
    }
}
