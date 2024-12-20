package individual.business.implementation;

import individual.domain.invoice.Invoice;
import individual.persistence.entity.InvoiceEntity;
import individual.persistence.entity.ServiceEntity;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceConverter {
    public static Invoice convert(InvoiceEntity invoiceEntity) {
        return Invoice.builder()
                .id(invoiceEntity.getInvoiceId())
                .amount(invoiceEntity.getTotalAmount())
                .description(invoiceEntity.getDescription())
                .issueDate(invoiceEntity.getIssueDate())
                .dueDate(invoiceEntity.getDueDate())
                .eventId(invoiceEntity.getEvent().getEventId())
                .serviceIds(invoiceEntity.getServices() == null ? List.of() :
                        invoiceEntity.getServices().stream()
                                .map(service -> service.getServiceId())
                                .collect(Collectors.toList()))

                .build();
    }
}
