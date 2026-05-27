package re.edu.model.dto.response.studentRes;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentResponse {

    private Integer id;

    private Integer userId;

    private String username;

    private String fullName;

    private String studentCode;

    private String major;

    private String className;

    private LocalDate dateOfBirth;

    private String address;
}