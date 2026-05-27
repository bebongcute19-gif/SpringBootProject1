package re.edu.model.dto.request.internshipPhaseReq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
@Getter
@Setter


public class UpdateInternshipPhase {

    private String phaseName;

    private String description;


    private LocalDate startDate;


    private LocalDate endDate;
}
