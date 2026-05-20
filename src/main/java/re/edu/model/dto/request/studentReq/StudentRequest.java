package re.edu.model.dto.request.studentReq;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRequest {

    @NotBlank
    private String studentCode;

    private String major;

    private String className;

    private LocalDate dateOfBirth;

    private String address;

    private Integer userId;
}