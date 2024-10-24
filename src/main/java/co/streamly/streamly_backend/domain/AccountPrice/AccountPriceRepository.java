package co.streamly.streamly_backend.domain.AccountPrice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountPriceRepository extends JpaRepository<AccountPrice, Long> {
    
    // Método para encontrar el precio más bajo asociado a un accountId
    Optional<AccountPrice> findTopByAccountIdOrderByPriceAsc(Long accountId);

    // Método para encontrar todos los precios asociados a un accountId
    List<AccountPrice> findByAccountId(Long accountId);

    // Método para encontrar un precio asociado a un accountId, meses y tipo específicos
    Optional<AccountPrice> findByAccountIdAndMonthsAndType(Long accountId, int months, TypeAccount type);
}
