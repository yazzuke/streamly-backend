package co.streamly.streamly_backend.controller;


import co.streamly.streamly_backend.domain.Metadata.ServiceMetadata;
import co.streamly.streamly_backend.service.ServiceMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/streamly/metadata")
public class ServiceMetadataController {

    private final ServiceMetadataService serviceMetadataService;

    @Autowired
    public ServiceMetadataController(ServiceMetadataService serviceMetadataService) {
        this.serviceMetadataService = serviceMetadataService;
    }

    // Obtener todos los metadatos de los servicios
    @GetMapping("/services")
    public ResponseEntity<List<ServiceMetadata>> getAllServiceMetadata() {
        List<ServiceMetadata> metadata = serviceMetadataService.getAllServiceMetadata();
        return ResponseEntity.ok(metadata);
    }

    // Crear un nuevo servicio metadata
    @PostMapping("/services")
    public ResponseEntity<ServiceMetadata> createServiceMetadata(@RequestBody ServiceMetadata serviceMetadata) {
        ServiceMetadata createdServiceMetadata = serviceMetadataService.createServiceMetadata(serviceMetadata);
        return ResponseEntity.ok(createdServiceMetadata);
    }

    // Eliminar un servicio metadata por su ID
    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteServiceMetadata(@PathVariable Long id) {
        boolean deleted = serviceMetadataService.deleteServiceMetadata(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}