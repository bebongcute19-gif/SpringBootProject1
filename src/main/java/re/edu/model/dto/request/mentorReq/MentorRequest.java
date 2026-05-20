package re.edu.model.dto.request.mentorReq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorRequest {

    @NotNull(message = "Mã người dùng không được để trống")
    private Integer userId;

    @NotBlank(message = "Khoa/Bộ môn không được để trống")
    private String department;

    @NotBlank(message = "Học hàm/Học vị không được để trống")
    private String academicRank;
}