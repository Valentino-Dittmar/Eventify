package individual.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "event")
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @OneToMany(mappedBy = "event")
    private List<ServiceEntity> services;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    //set it to this because of some lazyloading issues(it's not a good practice)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_attendants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> attendants;
}