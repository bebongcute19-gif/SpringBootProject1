package re.edu.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {
    @Id
    private Integer id;
    @OneToOne
    @MapsId
    @JoinColumn(name ="student_id")
    private User user;
    @Column(nullable = false, unique = true, length = 20)
    private String studentCode;
    @Column(length = 100)
    private String major;
    @Column(length = 50)
    private String className;
    private LocalDate dateOfBirth;
    @Column(length = 255)
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "student")
    private List<InternshipAssignment> assignments;
}
