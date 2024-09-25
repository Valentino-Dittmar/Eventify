package individual.business.implementation;

import individual.domain.service.GetAllServicesRequest;
import individual.domain.service.GetAllServicesResponse;
import individual.domain.service.Service;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetServicesUseCaseImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceConverter serviceConverter;

    @InjectMocks
    private GetAllServicesResponse serviceResponse;
    @Test
    public void getServices() {
        // Arrange
        GetAllServicesRequest request = new GetAllServicesRequest();
        request.setName("TestName");

        Service entity = new Service();

        // Act
        GetAllServicesResponse response = (GetAllServicesResponse) serviceResponse.getServices();
        // Assert
        assertNotNull(response);
        assertEquals(1, response.getServices().size());



    }
}