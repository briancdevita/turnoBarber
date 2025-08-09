package appbarber.repository;


import appbarber.model.auth.MagicLinkToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MagicLinkTokenRepository  extends CrudRepository<MagicLinkToken, Long> {
    Optional<MagicLinkToken> findByToken(String token);

}
