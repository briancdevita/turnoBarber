package appbarber.repository;

import appbarber.enums.EstadoTurno;
import appbarber.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    @Query("""
     select count(t) > 0
     from Turno t
     where t.empresa.id = :empresaId
       and t.barbero.id = :barberoId
       and t.estado <> appbarber.enums.EstadoTurno.CANCELADO
       and (t.inicio < :fin and t.fin > :inicio)
  """)
    boolean existsSolapado(Long empresaId, Long barberoId, LocalDateTime inicio, LocalDateTime fin);

    List<Turno> findByEmpresaIdAndBarberoIdAndInicioBetweenOrderByInicioAsc(
            Long empresaId, Long barberoId, LocalDateTime desde, LocalDateTime hasta
    );
}
