package individual.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEventRequest {
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Long creatorId;
    private List<Long> attendantIds;
}
