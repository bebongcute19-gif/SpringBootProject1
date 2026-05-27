package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.internshipassignmentreq.InternshipAssignmentRequest;
import re.edu.model.dto.request.internshipassignmentreq.UpdateAssignmentStatusRequest;
import re.edu.model.dto.response.internshipassignmentres.InternshipAssignmentResponse;
import re.edu.model.entity.*;
import re.edu.model.enums.AssignmentStatus;
import re.edu.model.enums.Role;
import re.edu.repository.assessmentRep.InternshipAssignmentRepository;
import re.edu.repository.internshipRep.InternshipPhaseRepository;
import re.edu.repository.userRep.MentorRepository;
import re.edu.repository.userRep.StudentRepository;
import re.edu.security.userDetail.CustomUserDetails;
import re.edu.service.InternshipAssignmentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements InternshipAssignmentService {

    private final InternshipAssignmentRepository assignmentRepository;

    private final StudentRepository studentRepository;

    private final MentorRepository mentorRepository;

    private final InternshipPhaseRepository phaseRepository;

    @Override
    public List<InternshipAssignmentResponse>
    getAllAssignments() {

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        User currentUser =
                userDetails.getUser();

        List<InternshipAssignment> assignments;

        // ===== ADMIN =====
        if (
                currentUser.getRole()
                        == Role.ADMIN
        ) {

            assignments =
                    assignmentRepository
                            .findAll();
        }

        // ===== MENTOR =====
        else if (
                currentUser.getRole()
                        == Role.MENTOR
        ) {

            assignments =
                    assignmentRepository
                            .findByMentor_Id(
                                    currentUser.getId()
                            );
        }

        // ===== STUDENT =====
        else if (
                currentUser.getRole()
                        == Role.STUDENT
        ) {

            assignments =
                    assignmentRepository
                            .findByStudent_Id(
                                    currentUser.getId()
                            );
        }

        else {

            throw new IllegalArgumentException(
                    "Không có quyền truy cập"
            );
        }

        return assignments
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InternshipAssignmentResponse
    getAssignmentById(
            Integer assignmentId
    ) {

        InternshipAssignment assignment =
                assignmentRepository
                        .findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Không tìm thấy phân công thực tập"
                                )
                        );

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        User currentUser =
                userDetails.getUser();

        // ===== ADMIN =====
        if (
                currentUser.getRole()
                        == Role.ADMIN
        ) {

            return toResponse(assignment);
        }

        // ===== MENTOR =====
        if (
                currentUser.getRole()
                        == Role.MENTOR
        ) {

            if (
                    !assignment.getMentor()
                            .getId()
                            .equals(currentUser.getId())
            ) {

                throw new AccessDeniedExceptionCustom(
                        "Bạn không có quyền truy cập"
                );
            }

            return toResponse(assignment);
        }

        // ===== STUDENT =====
        if (
                currentUser.getRole()
                        == Role.STUDENT
        ) {

            if (
                    !assignment.getStudent()
                            .getId()
                            .equals(currentUser.getId())
            ) {

                throw new AccessDeniedExceptionCustom(
                        "Bạn không có quyền truy cập"
                );
            }

            return toResponse(assignment);
        }

        throw new AccessDeniedExceptionCustom(
                "Bạn không có quyền truy cập"
        );
    }

    @Override
    public InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request) {

        validateRequest(request);

        boolean exists = assignmentRepository.findByStudent_IdAndPhase_PhaseId(request.getStudentId(), request.getPhaseId()).isPresent();

        if (exists) {

            throw new IllegalArgumentException("Sinh viên đã được phân công trong giai đoạn này");
        }

        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        Mentor mentor = mentorRepository.findById(request.getMentorId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên hướng dẫn"));

        InternshipPhase phase = phaseRepository.findById(request.getPhaseId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

        InternshipAssignment assignment = new InternshipAssignment();

        assignment.setStudent(student);

        assignment.setMentor(mentor);

        assignment.setPhase(phase);

        assignment.setAssignedDate(LocalDateTime.now());

        assignment.setStatus(AssignmentStatus.PENDING);

        assignment.setCreatedAt(LocalDateTime.now());

        assignment.setUpdatedAt(LocalDateTime.now());

        return toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public InternshipAssignmentResponse updateAssignmentStatus(Integer assignmentId, UpdateAssignmentStatusRequest request) {

        InternshipAssignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phân công thực tập"));

        if (request.getStatus() == null) {

            throw new IllegalArgumentException("Trạng thái không được để trống");
        }

        assignment.setStatus(request.getStatus());

        assignment.setUpdatedAt(LocalDateTime.now());

        return toResponse(assignmentRepository.save(assignment));
    }

    private InternshipAssignmentResponse toResponse(InternshipAssignment assignment) {

        InternshipAssignmentResponse response = new InternshipAssignmentResponse();

        response.setAssignmentId(assignment.getAssignmentId());

        response.setStudentId(assignment.getStudent().getId());

        response.setStudentName(assignment.getStudent().getUser().getFullName());

        response.setMentorId(assignment.getMentor().getId());

        response.setMentorName(assignment.getMentor().getUser().getFullName());

        response.setPhaseId(assignment.getPhase().getPhaseId());

        response.setPhaseName(assignment.getPhase().getPhaseName());

        response.setAssignedDate(assignment.getAssignedDate());

        response.setStatus(assignment.getStatus());

        return response;
    }

    private void validateRequest(InternshipAssignmentRequest request) {

        if (request.getStudentId() == null) {

            throw new IllegalArgumentException("Mã sinh viên không được để trống");
        }

        if (request.getMentorId() == null) {

            throw new IllegalArgumentException("Mã giảng viên hướng dẫn không được để trống");
        }

        if (request.getPhaseId() == null) {

            throw new IllegalArgumentException("Mã giai đoạn thực tập không được để trống");
        }
    }
}