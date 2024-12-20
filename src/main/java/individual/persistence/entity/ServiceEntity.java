package individual.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Entity
@Table(name = "service")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private BigDecimal duration;

    // Many services can belong to one event
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    // Many services can belong to one invoice
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;


}
