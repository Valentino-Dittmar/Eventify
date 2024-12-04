package individual.business.implementation;

import individual.business.CreateInvoiceUseCase;
import individual.domain.invoice.CreateInvoiceRequest;
import individual.domain.invoice.CreateInvoiceResponse;
import individual.persistence.InvoiceRepository;
import individual.persistence.ServiceRepository;
import individual.persistence.EventRepository;
import individual.persistence.UserRepository;
import individual.persistence.entity.InvoiceEntity;
import individual.persistence.entity.ServiceEntity;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateInvoiceUseCaseImpl implements CreateInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;
    private final ServiceRepository serviceRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository; // Add user repository

    @Transactional
    @Override
    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest request) {
        EventEntity event = eventRepository.findByEventId(request.getEventId());
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }

        UserEntity user = userRepository.findByUserId(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<ServiceEntity> services = serviceRepository.findAllById(request.getServiceIds());
        if (services.isEmpty()) {
            throw new IllegalArgumentException("No services found for the given IDs");
        }

        InvoiceEntity invoice = InvoiceEntity.builder()
                .totalAmount(request.getTotalAmount())
                .description(request.getDescription())
                .issueDate(request.getIssueDate())
                .dueDate(request.getDueDate())
                .user(user)
                .services(services)
                .event(event)
                .build();

        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        return CreateInvoiceResponse.builder()
                .invoiceId(savedInvoice.getInvoiceId())
                .build();
    }
}