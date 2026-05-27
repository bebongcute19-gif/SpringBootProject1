package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.studentReq.StudentRequest;
import re.edu.model.dto.request.studentReq.UpdateStudent;
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

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User currentUser = userDetails.getUser();

        List<Student> students;

        // ADMIN xem toàn bộ
        if (currentUser.getRole() == re.edu.model.enums.Role.ADMIN) {

            students = studentRepository.findAll();
        }

        // MENTOR chỉ xem sinh viên được phân công
        else if (currentUser.getRole() == re.edu.model.enums.Role.MENTOR) {

            students = studentRepository
                    .findDistinctByAssignments_Mentor_Id(
                            currentUser.getId()
                    );
        }

        else {

            throw new AccessDeniedExceptionCustom(
                    "Bạn không có quyền truy cập"
            );
        }

        return students.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StudentResponse getStudentById(Integer studentId) {

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        checkViewPermission(student);

        return toResponse(student);
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByStudentCode(
                request.getStudentCode()
        )) {

            throw new DuplicateResourceException(
                    "Mã sinh viên đã tồn tại"
            );
        }
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // check role
        if (user.getRole() != re.edu.model.enums.Role.STUDENT) {

            throw new IllegalArgumentException(
                    "Người dùng phải có vai trò STUDENT"
            );
        }
        // check student existed
        if (studentRepository.existsById(
                user.getId()
        )) {

            throw new DuplicateResourceException(
                    "Sinh viên đã tồn tại"
            );
        }

        Student student = new Student();

        student.setUser(user);
        student.setStudentCode(request.getStudentCode());
        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        return toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse updateStudent(Integer studentId, UpdateStudent request) {

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        checkUpdatePermission(student);

        if (request.getStudentCode() != null
                && !request.getStudentCode().isBlank()) {

            // check duplicate
            if (!student.getStudentCode().equals(request.getStudentCode())
                    && studentRepository.existsByStudentCode(
                    request.getStudentCode()
            )) {

                throw new DuplicateResourceException(
                        "Mã sinh viên đã tồn tại"
                );
            }

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

        return toResponse(studentRepository.save(student));
    }

    private StudentResponse toResponse(Student student) {

        StudentResponse response = modelMapper.map(student, StudentResponse.class);

        response.setUserId(student.getUser().getId());
        response.setUsername(student.getUser().getUsername());
        response.setFullName(student.getUser().getFullName());

        return response;
    }

    private void checkViewPermission(Student student) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_STUDENT")) {

            if (!student.getUser().getId().equals(userDetails.getUser().getId())) {

                throw new AccessDeniedExceptionCustom("Bạn chỉ có thể xem thông tin của chính mình");
            }
        }
    }

    private void checkUpdatePermission(Student student) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_STUDENT")) {

            if (!student.getUser().getId().equals(userDetails.getUser().getId())) {

                throw new AccessDeniedExceptionCustom("Bạn chỉ có thể cập nhật thông tin của chính mình");
            }
        }
    }
}