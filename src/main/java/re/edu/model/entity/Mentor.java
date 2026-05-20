package re.edu.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name ="mentors")
public class Mentor {
    @Id
    private Integer id;
    @OneToOne
    @MapsId
    @JoinColumn(name ="mentor_id")
    private User user;
    @Column(length = 100)
    private String department;
    @Column(length = 50)
    private String academicRank;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "mentor")
    private List<InternshipAssignment> assignments;
}
