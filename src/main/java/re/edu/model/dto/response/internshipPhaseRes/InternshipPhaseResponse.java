package re.edu.model.dto.response.internshipPhaseRes;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InternshipPhaseResponse {

    private Integer id;

    private String phaseName;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;
}