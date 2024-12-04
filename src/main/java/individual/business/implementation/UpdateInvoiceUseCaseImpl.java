package individual.business.implementation;

import individual.business.UpdateInvoiceUseCase;
import individual.domain.invoice.UpdateInvoiceRequest;
import individual.persistence.InvoiceRepository;
import individual.persistence.ServiceRepository;
import individual.persistence.EventRepository;
import individual.persistence.entity.InvoiceEntity;
import individual.persistence.entity.ServiceEntity;
import individual.persistence.entity.EventEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UpdateInvoiceUseCaseImpl implements UpdateInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;
    private final ServiceRepository serviceRepository;
    private final EventRepository eventRepository;
    @Transactional
    @Override
    public InvoiceEntity updateInvoice(UpdateInvoiceRequest request) {
        InvoiceEntity invoice = invoiceRepository.findByInvoiceId(request.getInvoiceId());

        if (request.getTotalAmount() != null) {
            invoice.setTotalAmount(request.getTotalAmount());
        }
        if (request.getDescription() != null) {
            invoice.setDescription(request.getDescription());
        }
        if (request.getIssueDate() != null) {
            invoice.setIssueDate(request.getIssueDate());
        }
        if (request.getDueDate() != null) {
            invoice.setDueDate(request.getDueDate());
        }
        if (request.getServiceIds() != null) {
            List<ServiceEntity> services = serviceRepository.findAllById(request.getServiceIds());
            invoice.setServices(services);
        }
        if (request.getEventId() != null) {
            EventEntity event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Event not found"));
            invoice.setEvent(event);
        }

        return invoiceRepository.save(invoice);
    }
}