package re.edu.service;


import re.edu.model.dto.request.internshipassignmentreq.InternshipAssignmentRequest;
import re.edu.model.dto.request.internshipassignmentreq.UpdateAssignmentStatusRequest;
import re.edu.model.dto.response.internshipassignmentres.InternshipAssignmentResponse;

import java.util.List;

public interface InternshipAssignmentService {

    List<InternshipAssignmentResponse> getAllAssignments();

    InternshipAssignmentResponse getAssignmentById(Integer assignmentId);

    InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request);

    InternshipAssignmentResponse updateAssignmentStatus(Integer assignmentId, UpdateAssignmentStatusRequest request);
}