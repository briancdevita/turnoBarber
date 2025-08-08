package appbarber.repository;


import appbarber.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmpresaIdAndEmailIgnoreCase(Long empresaId, String email);
}
