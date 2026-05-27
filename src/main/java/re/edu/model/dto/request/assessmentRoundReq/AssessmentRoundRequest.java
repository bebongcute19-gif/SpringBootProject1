package re.edu.model.dto.request.assessmentRoundReq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import re.edu.model.dto.request.roundcriteriareq.RoundCriteriaRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AssessmentRoundRequest {
    @NotBlank(message = "Tên đợt đánh giá không được để trống")
    private String roundName;
    private String description;
    @NotNull(message = "phaseId không được để trống")
    private Integer phaseId;
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;
    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;
    @NotNull(message = "Trạng thái không được để trống")
    private Boolean isActive;
    @NotEmpty(message = "Danh sách tiêu chí không được để trống")
    private List<RoundCriteriaRequest> criteria;
}