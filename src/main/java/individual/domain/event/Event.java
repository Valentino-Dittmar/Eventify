package individual.domain.event;

import individual.domain.User.User;
import individual.domain.service.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Long eventId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private List<Service> services;
    private Long creatorId;
    private List<User> attendants;
}
