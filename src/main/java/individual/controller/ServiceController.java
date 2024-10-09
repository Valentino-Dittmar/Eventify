package individual.controller;

import individual.business.*;
import individual.domain.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/services")
@AllArgsConstructor
public class ServiceController {
    private final GetServiceUseCase getServiceUseCase;
    private final GetServicesUseCase getServicesUseCase;
    private final DeleteServiceUseCase deleteServiceUseCase;
    private final CreateServiceUseCase createServiceUseCase;
    private final UpdateServiceUseCase updateServiceUseCase;
    @GetMapping("{id}")
    public ResponseEntity<Service> getService(@PathVariable(value = "id") final long id) {
        final Optional<Service> serviceOptional = getServiceUseCase.findById(id);
        if (serviceOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(serviceOptional.get());
    }
    @GetMapping
    public ResponseEntity<GetAllServicesResponse> getAllServices(@RequestParam(value = "name", required = false) String serviceName) {
        GetAllServicesRequest request = GetAllServicesRequest.builder().name(serviceName).build();
        GetAllServicesResponse response = getServicesUseCase.getServices(request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable long serviceId) {
        deleteServiceUseCase.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping()
    public ResponseEntity<CreateServiceResponse> createService(@RequestBody @Valid CreateServiceRequest request) {
        CreateServiceResponse response = createServiceUseCase.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("{id}")
    public ResponseEntity<Void> updateService(@PathVariable("id") long id,
                                              @RequestBody @Valid UpdateServiceRequest request) {
        request.setServiceId(id);
        updateServiceUseCase.updateService(request);
        return ResponseEntity.noContent().build();
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/services")
    public ResponseEntity<GetAllServicesResponse> getAllServicesApi(@RequestParam(value = "name", required = false) String serviceName) {
        GetAllServicesRequest request = GetAllServicesRequest.builder().name(serviceName).build();
        GetAllServicesResponse response = getServicesUseCase.getServices(request);
        return ResponseEntity.ok(response);
    }
}
