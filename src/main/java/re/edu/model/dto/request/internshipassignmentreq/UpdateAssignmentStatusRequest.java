package re.edu.model.dto.request.internshipassignmentreq;

import lombok.Getter;
import lombok.Setter;
import re.edu.model.enums.AssignmentStatus;

@Getter
@Setter
public class UpdateAssignmentStatusRequest {

    private AssignmentStatus status;
}