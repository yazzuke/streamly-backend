package co.streamly.streamly_backend.domain.Combo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {
}
