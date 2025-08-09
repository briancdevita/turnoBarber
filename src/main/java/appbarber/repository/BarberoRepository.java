package appbarber.repository;

import appbarber.model.Barbero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BarberoRepository extends JpaRepository<Barbero, Long> {
    List<Barbero> findByEmpresaIdOrderByNombreAsc(Long empresaId);
    List<Barbero> findByEmpresaIdAndActivoTrueOrderByNombreAsc(Long empresaId);

    Page<Barbero> findByEmpresaIdAndNombreContainingIgnoreCase(Long empresaId, String nombre, Pageable pageable);
    boolean existsByEmpresaIdAndNombreIgnoreCase(Long empresaId, String nombre);
    boolean existsByEmpresaIdAndNombreIgnoreCaseAndIdNot(Long empresaId, String nombre, Long id);


}
