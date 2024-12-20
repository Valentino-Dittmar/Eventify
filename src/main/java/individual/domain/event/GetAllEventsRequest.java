package individual.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllEventsRequest {
    private Long eventId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Long creatorId;
    private List<Long> serviceIds;
    private List<Long> attendantIds;
}
