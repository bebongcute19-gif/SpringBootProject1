package re.edu.model.dto.request.studentReq;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudent {
    private String studentCode;

    private String major;

    private String className;

    private LocalDate dateOfBirth;

    private String address;

    private Integer userId;
}
