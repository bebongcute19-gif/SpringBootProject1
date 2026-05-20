package re.edu.model.dto.request.mentorReq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorRequest {

    @NotNull(message = "User id is required")
    private Integer userId;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Academic rank is required")
    private String academicRank;
}