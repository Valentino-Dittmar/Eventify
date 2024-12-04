package individual.controller;

import individual.business.*;
import individual.domain.invoice.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/invoices")
@PreAuthorize("hasRole('CUSTOMER')")
@AllArgsConstructor
public class InvoiceController {
    private final GetInvoiceUseCase getInvoiceUseCase;
    private final GetInvoicesUseCase getInvoicesUseCase;
    private final DeleteInvoiceUseCase deleteInvoiceUseCase;
    private final CreateInvoiceUseCase createInvoiceUseCase;
    private final UpdateInvoiceUseCase updateInvoiceUseCase;

    @GetMapping("{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable(value = "id") final long id) {
        final Optional<Invoice> invoiceOptional = getInvoiceUseCase.getInvoiceById(id);
        if (invoiceOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(invoiceOptional.get());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public ResponseEntity<GetAllInvoicesResponse> getAllInvoices() {
        GetAllInvoicesResponse response = getInvoicesUseCase.getAllInvoices();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable long invoiceId) {
        deleteInvoiceUseCase.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CreateInvoiceResponse> createInvoice(@RequestBody @Valid CreateInvoiceRequest request) {
        CreateInvoiceResponse response = createInvoiceUseCase.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateInvoice(@PathVariable("id") long id,
                                              @RequestBody @Valid UpdateInvoiceRequest request) {
        request.setInvoiceId(id);
        updateInvoiceUseCase.updateInvoice(request);
        return ResponseEntity.noContent().build();
    }
}