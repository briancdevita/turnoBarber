package appbarber.service;


import appbarber.DTO.MagicLinkRequest;
import appbarber.DTO.MagicLinkResponse;
import appbarber.DTO.TurnoDTO;
import appbarber.DTO.TurnoMapper;
import appbarber.enums.EstadoTurno;
import appbarber.exception.NotFoundException;
import appbarber.model.auth.MagicLinkToken;
import appbarber.repository.ClienteRepository;
import appbarber.repository.EmpresaRepository;
import appbarber.repository.MagicLinkTokenRepository;
import appbarber.repository.TurnoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MagicLinkService {

    private final EmpresaRepository empresaRepo;
    private final ClienteRepository clienteRepo;
    private final MagicLinkTokenRepository tokenRepo;
    private final TurnoRepository turnoRepo;

    public MagicLinkService(EmpresaRepository empresaRepo, ClienteRepository clienteRepo,
                            MagicLinkTokenRepository tokenRepo, TurnoRepository turnoRepo) {
        this.empresaRepo = empresaRepo;
        this.clienteRepo = clienteRepo;
        this.tokenRepo = tokenRepo;
        this.turnoRepo = turnoRepo;
    }


    @Transactional
    public MagicLinkResponse solicitar(MagicLinkRequest req) {
        var empresa = empresaRepo.findById(req.empresaId())
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada"));
        var cliente = clienteRepo.findAll().stream()
                .filter(c -> c.getEmpresa().getId().equals(empresa.getId())
                             && c.getEmail() != null
                             && c.getEmail().equalsIgnoreCase(req.emailCliente()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado en la empresa"));

        var token = UUID.randomUUID().toString().replace("-", "");
        var m = MagicLinkToken.builder()
                .empresa(empresa)
                .cliente(cliente)
                .token(token)
                .expiraEn(LocalDateTime.now().plusMinutes(30))
                .build();
        tokenRepo.save(m);

        // En producción: enviar por email. Aquí devolvemos el token para probar.
        return new MagicLinkResponse(token, "En producción se enviaría por email.");
    }

    @Transactional()
    public List<TurnoDTO> turnos(String token) {
        var t = tokenRepo.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Token inválido"));
        if (!t.isVigente()) throw new IllegalArgumentException("Token vencido o usado");
        var desde = LocalDateTime.now().minusDays(30);
        var hasta = LocalDateTime.now().plusDays(60);
        return turnoRepo.findByEmpresaIdAndClienteIdAndInicioBetweenOrderByInicioAsc(
                t.getEmpresa().getId(), t.getCliente().getId(), desde, hasta
        ).stream().map(TurnoMapper::toDTO).toList();
    }

    @Transactional
    public void cancelar(String token, Long turnoId) {
        var tkn = tokenRepo.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Token inválido"));
        if (!tkn.isVigente()) throw new IllegalArgumentException("Token vencido o usado");

        var turno = turnoRepo.findById(turnoId)
                .orElseThrow(() -> new NotFoundException("Turno no encontrado"));
        if (!turno.getEmpresa().getId().equals(tkn.getEmpresa().getId())
            || !turno.getCliente().getId().equals(tkn.getCliente().getId())) {
            throw new IllegalArgumentException("El turno no pertenece al cliente/empresa del token");
        }

        // Reutilizamos tu regla de 2h (puedes llamar a TurnoService.cancelar, aquí lo hacemos inline rápido)
        if (turno.getInicio().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Debe cancelar con al menos 2 horas de anticipación.");
        }
        turno.setEstado(EstadoTurno.CANCELADO);
    }

}
