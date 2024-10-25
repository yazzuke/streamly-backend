package co.streamly.streamly_backend.domain.Metadata;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", unique = true)
    private String serviceName;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "svg_url") // Agregar el SVG URL en los metadatos
    private String svgUrl;
}