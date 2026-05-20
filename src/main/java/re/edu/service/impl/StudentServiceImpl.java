package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.studentReq.StudentRequest;
import re.edu.model.dto.response.studentRes.StudentResponse;
import re.edu.model.entity.Student;
import re.edu.model.entity.User;
import re.edu.repository.userRep.StudentRepository;
import re.edu.repository.userRep.UserRepository;
import re.edu.security.userDetail.CustomUserDetails;
import re.edu.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StudentResponse> getAllStudents() {

        List<Student> students = studentRepository.findAll();

        return students.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StudentResponse getStudentById(Integer studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        checkViewPermission(student);

        return toResponse(student);
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Student student = new Student();

        student.setUser(user);
        student.setStudentCode(request.getStudentCode());
        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        return toResponse(
                studentRepository.save(student)
        );
    }

    @Override
    public StudentResponse updateStudent(Integer studentId,
                                         StudentRequest request) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        checkUpdatePermission(student);

        if (request.getStudentCode() != null) {
            student.setStudentCode(request.getStudentCode());
        }

        if (request.getMajor() != null) {
            student.setMajor(request.getMajor());
        }

        if (request.getClassName() != null) {
            student.setClassName(request.getClassName());
        }

        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getAddress() != null) {
            student.setAddress(request.getAddress());
        }

        return toResponse(
                studentRepository.save(student)
        );
    }

    private StudentResponse toResponse(Student student) {

        StudentResponse response =
                modelMapper.map(student, StudentResponse.class);

        response.setUserId(student.getUser().getId());
        response.setUsername(student.getUser().getUsername());

        return response;
    }

    private void checkViewPermission(Student student) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_STUDENT")) {

            if (!student.getUser().getId()
                    .equals(userDetails.getUser().getId())) {

                throw new RuntimeException("Access denied");
            }
        }
    }

    private void checkUpdatePermission(Student student) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_STUDENT")) {

            if (!student.getUser().getId()
                    .equals(userDetails.getUser().getId())) {

                throw new RuntimeException("Access denied");
            }
        }
    }
}