package appbarber.service.auth;


import appbarber.DTO.LoginRequest;
import appbarber.DTO.LoginResponse;
import appbarber.exception.NotFoundException;
import appbarber.model.auth.Usuario;
import appbarber.repository.UsuarioRepository;
import appbarber.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UsuarioRepository repo, PasswordEncoder encoder, JwtService jwt) {
        this.repo = repo; this.encoder = encoder; this.jwt = jwt;
    }

    public LoginResponse login(LoginRequest req) {
        Usuario u = repo.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        if (!u.isActivo() || !encoder.matches(req.password(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        var token = jwt.generate(u.getEmail(), Map.of("rol", u.getRol().name()));
        return new LoginResponse(token);
    }


}
