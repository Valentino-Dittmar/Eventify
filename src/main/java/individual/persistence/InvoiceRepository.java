package individual.persistence;

import individual.persistence.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    void deleteByInvoiceId(Long userId);
    InvoiceEntity findByInvoiceId(Long invoiceId);
    @Query("SELECT i FROM InvoiceEntity i JOIN FETCH i.services")
    List<InvoiceEntity> findAllWithServices();
}
