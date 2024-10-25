package co.streamly.streamly_backend.domain.ComboPrice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComboPriceRepository extends JpaRepository<ComboPrice, Long> {

    List<ComboPrice> findByComboId(Long comboId);

    Optional<ComboPrice> findTopByComboIdOrderByPriceAsc(Long comboId);

    Optional<ComboPrice> findByComboIdAndMonthsAndType(Long comboId, int months, TypeCombo type);
}
