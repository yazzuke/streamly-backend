package co.streamly.streamly_backend.domain.Metadata;

import co.streamly.streamly_backend.domain.Metadata.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceMetadataRepository extends JpaRepository<ServiceMetadata, Long> {
    Optional<ServiceMetadata> findByServiceName(String serviceName);
}
