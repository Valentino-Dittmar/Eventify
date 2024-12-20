package individual.persistence.implementation;

import individual.persistence.entity.ServiceEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
//Keeping this in case of custom implementation needed in the future
@Repository
public class ServiceRepositoryImpl {
    private static long NEXT_ID = 1;
    private final List<ServiceEntity> savedServices;

    public ServiceRepositoryImpl() {
        this.savedServices = new ArrayList<>();
    }


}

