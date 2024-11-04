package co.streamly.streamly_backend.domain.Stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Aquí puedes definir métodos adicionales de consulta si lo necesitas
}