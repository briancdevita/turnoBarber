package appbarber.repository;

import appbarber.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByEmpresaIdOrderByNombreAsc(Long empresaId);
    List<Servicio> findByEmpresaIdAndActivoTrueOrderByNombreAsc(Long empresaId);
}
