package individual.business.implementation;


import individual.domain.service.Service;
import individual.persistence.entity.ServiceEntity;

public class ServiceConverter {
    private ServiceConverter() {}
    public static Service convert(ServiceEntity service) {
        return Service.builder()
                .id(service.getServiceId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .duration(service.getDuration())
                .eventId(service.getEvent() != null ? service.getEvent().getEventId() : null)
                .build();
    }
}
