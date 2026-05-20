package re.edu.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import re.edu.model.enums.Role;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, length = 255)
    private String passwordHash;
    @Column(nullable = false, length = 100)
    private String fullName;
    @Column(nullable = false,unique = true, length = 100)
    private String email;
    @Column(length = 20)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isActive=true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Mentor mentor;
}
