package individual.business.exception;

public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(Long serviceId) {
        super("Service with ID " + serviceId + " not found");
    }
}
