package re.edu.model.dto.response.internshipassignmentres;

import lombok.Getter;
import lombok.Setter;
import re.edu.model.enums.AssignmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class InternshipAssignmentResponse {

    private Integer assignmentId;

    private Integer studentId;

    private String studentName;

    private Integer mentorId;

    private String mentorName;

    private Integer phaseId;

    private String phaseName;

    private LocalDateTime assignedDate;

    private AssignmentStatus status;
}