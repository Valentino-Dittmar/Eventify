package individual.business.implementation;

import individual.domain.service.GetAllServicesResponse;
import individual.persistence.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GetServicesUseCaseImplTest {

    @Test
    public void simpleTest() {
        // Arrange
        int a = 1;
        int b = 1;

        // Act
        int sum = a + b;

        // Assert
        assertEquals(2, sum);
    }


}