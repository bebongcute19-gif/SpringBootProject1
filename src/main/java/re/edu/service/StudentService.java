package re.edu.service;



import re.edu.model.dto.request.studentReq.StudentRequest;
import re.edu.model.dto.request.studentReq.UpdateStudent;
import re.edu.model.dto.response.studentRes.StudentResponse;

import java.util.List;

public interface StudentService {

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(Integer studentId);

    StudentResponse createStudent(StudentRequest request);

    StudentResponse updateStudent(Integer studentId, UpdateStudent request);
}