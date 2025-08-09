package appbarber.repository;


import appbarber.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmpresaIdAndEmailIgnoreCase(Long empresaId, String email);

    Page<Cliente> findByEmpresaIdAndNombreContainingIgnoreCase(Long empresaId, String nombre, Pageable pageable);
    Page<Cliente> findByEmpresaIdAndEmailContainingIgnoreCase(Long empresaId, String email, Pageable pageable);
    Page<Cliente> findByEmpresaIdAndTelefonoContainingIgnoreCase(Long empresaId, String tel, Pageable pageable);
}
