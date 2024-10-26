package co.streamly.streamly_backend.service;

import co.streamly.streamly_backend.domain.Metadata.ServiceMetadata;
import co.streamly.streamly_backend.domain.Metadata.ServiceMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceMetadataService {
    
    private final ServiceMetadataRepository serviceMetadataRepository;

    @Autowired
    public ServiceMetadataService(ServiceMetadataRepository serviceMetadataRepository) {
        this.serviceMetadataRepository = serviceMetadataRepository;
    }

    public List<ServiceMetadata> getAllServiceMetadata() {
        return serviceMetadataRepository.findAll();
    }

    public ServiceMetadata createServiceMetadata(ServiceMetadata serviceMetadata) {
        return serviceMetadataRepository.save(serviceMetadata);
    }

    public boolean deleteServiceMetadata(Long id) {
        Optional<ServiceMetadata> metadata = serviceMetadataRepository.findById(id);
        if (metadata.isPresent()) {
            serviceMetadataRepository.delete(metadata.get());
            return true;
        } else {
            return false;
        }
    }
}
