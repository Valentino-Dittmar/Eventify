package individual.business.implementation.IntegrationTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import individual.business.*;
//import individual.business.implementation.TestSecurityConfig;
import individual.domain.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@Import(TestSecurityConfig.class)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:h2:mem:testdb",
//        "spring.datasource.driver-class-name=org.h2.Driver",
//        "spring.datasource.username=sa",
//        "spring.datasource.password=",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//        "spring.jpa.show-sql=true",
//        "spring.jpa.properties.hibernate.format_sql=true"
//})
class ServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    //Simulates HTTP Requests so that I can test the controller ;)

    @MockBean
    private GetServiceUseCase getServiceUseCase;

    @MockBean
    private GetServicesUseCase getServicesUseCase;

    @MockBean
    private DeleteServiceUseCase deleteServiceUseCase;

    @MockBean
    private CreateServiceUseCase createServiceUseCase;

    @MockBean
    private UpdateServiceUseCase updateServiceUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnServiceWhenFound() throws Exception {
        // Arrange
        long serviceId = 1L;
        Service service = Service.builder()
                .id(serviceId)
                .name("Test Service")
                .description("Test Description")
                .build();

        when(getServiceUseCase.findById(serviceId)).thenReturn(Optional.of(service));

        // Act & Assert
        mockMvc.perform(get("/services/{id}", serviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(serviceId))
                .andExpect(jsonPath("$.name").value("Test Service"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(getServiceUseCase, times(1)).findById(serviceId);
    }

    @Test
    void shouldReturnNotFoundWhenServiceNotFound() throws Exception {
        // Arrange
        long serviceId = 1L;

        when(getServiceUseCase.findById(serviceId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/services/{id}", serviceId))
                .andExpect(status().isNotFound());

        verify(getServiceUseCase, times(1)).findById(serviceId);
    }

    @Test
    void shouldReturnAllServices() throws Exception {
        // Arrange
        GetAllServicesResponse response = GetAllServicesResponse.builder()
                .services(List.of(
                        Service.builder().id(1L).name("Service 1").build(),
                        Service.builder().id(2L).name("Service 2").build()
                ))
                .build();

        when(getServicesUseCase.getServices(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.services").isArray())
                .andExpect(jsonPath("$.services[0].name").value("Service 1"))
                .andExpect(jsonPath("$.services[1].name").value("Service 2"));

        verify(getServicesUseCase, times(1)).getServices(any());
    }

    @Test
    void shouldDeleteServiceSuccessfully() throws Exception {
        // Arrange
        long serviceId = 1L;

        doNothing().when(deleteServiceUseCase).deleteService(serviceId);

        // Act & Assert
        mockMvc.perform(delete("/services/{serviceId}", serviceId))
                .andExpect(status().isNoContent());

        verify(deleteServiceUseCase, times(1)).deleteService(serviceId);
    }

    @Test
    void shouldCreateServiceSuccessfully() throws Exception {
        // Arrange
        CreateServiceRequest request = CreateServiceRequest.builder()
                .name("New Service")
                .description("New Description")
                .build();

        CreateServiceResponse response = CreateServiceResponse.builder()
                .serviceId(1L)
                .build();

        when(createServiceUseCase.createService(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serviceId").value(1L));

        verify(createServiceUseCase, times(1)).createService(any(CreateServiceRequest.class));
    }

    @Test
    void shouldUpdateServiceSuccessfully() throws Exception {
        // Arrange
        long serviceId = 1L;
        UpdateServiceRequest request = UpdateServiceRequest.builder()
                .name("Updated Service")
                .description("Updated Description")
                .build();

        doNothing().when(updateServiceUseCase).updateService(any());

        // Act & Assert
        mockMvc.perform(put("/services/{id}", serviceId)
                //setting content type header so that it knows how to treat the body
                        .contentType(MediaType.APPLICATION_JSON)
                //serialized into json
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(updateServiceUseCase, times(1)).updateService(any(UpdateServiceRequest.class));
    }
}