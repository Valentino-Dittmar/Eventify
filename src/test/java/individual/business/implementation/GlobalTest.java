package individual.business.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public abstract class GlobalTest {
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}
