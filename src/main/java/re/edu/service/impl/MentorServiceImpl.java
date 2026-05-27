package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.mentorReq.MentorRequest;
import re.edu.model.dto.request.mentorReq.UpdateMentorRequest;
import re.edu.model.dto.response.mentorRes.MentorResponse;
import re.edu.model.entity.Mentor;
import re.edu.model.entity.User;
import re.edu.model.enums.Role;
import re.edu.repository.assessmentRep.InternshipAssignmentRepository;
import re.edu.repository.userRep.MentorRepository;
import re.edu.repository.userRep.UserRepository;
import re.edu.security.userDetail.CustomUserDetails;
import re.edu.service.MentorService;
import re.edu.model.dto.response.mentorRes.MentorPublicResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    private final UserRepository userRepository;
    private final InternshipAssignmentRepository internshipAssignmentRepository;

    @Override
    public boolean existsByMentor_Id(Integer mentorId) {
        return false;
    }

    @Override
    public List<?> getAllMentors() {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User currentUser = userDetails.getUser();

        // ADMIN xem full thông tin
        if (currentUser.getRole() == Role.ADMIN) {

            return mentorRepository.findAll(Sort.by("id"))
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        // STUDENT chỉ xem thông tin public
        if (currentUser.getRole() == Role.STUDENT) {

            List<Mentor> mentors =
                    internshipAssignmentRepository
                            .findDistinctMentorsByStudent_Id(
                                    currentUser.getId()
                            );

            return mentors.stream()
                    .map(this::toPublicResponse)
                    .toList();
        }

        throw new AccessDeniedExceptionCustom(
                "Bạn không có quyền truy cập"
        );
    }
    @Override
    public MentorResponse getMentorById(Integer mentorId) {

        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy giảng viên hướng dẫn"
                        )
                );

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User currentUser = userDetails.getUser();

        /**
         * ADMIN xem tất cả
         */
        if (currentUser.getRole() == Role.ADMIN) {

            return toResponse(mentor);
        }

        /**
         * MENTOR chỉ xem chính mình
         */
        if (currentUser.getRole() == Role.MENTOR) {

            if (!mentor.getId().equals(currentUser.getId())) {

                throw new AccessDeniedExceptionCustom(
                        "Bạn chỉ có thể xem thông tin của chính mình"
                );
            }

            return toResponse(mentor);
        }

        /**
         * STUDENT chỉ xem mentor hướng dẫn mình
         */
        if (currentUser.getRole() == Role.STUDENT) {

            boolean assigned =
                    internshipAssignmentRepository
                            .existsByStudent_IdAndMentor_Id(
                                    currentUser.getId(),
                                    mentorId
                            );

            if (!assigned) {

                throw new AccessDeniedExceptionCustom(
                        "Bạn chỉ có thể xem giảng viên hướng dẫn của mình"
                );
            }

            return toResponse(mentor);
        }

        throw new AccessDeniedExceptionCustom(
                "Bạn không có quyền truy cập"
        );
    }
    @Override
    public MentorResponse createMentor(MentorRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy người dùng"
                        )
                );
        if (mentorRepository.existsById(
                user.getId()
        )) {

            throw new DuplicateResourceException(
                    "Giảng viên đã tồn tại"
            );
        }
        if (user.getRole() != Role.MENTOR) {

            throw new IllegalArgumentException(
                    "Vai trò của người dùng phải là MENTOR"
            );
        }

        Mentor mentor = new Mentor();

        mentor.setUser(user);
        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());
        mentor.setCreatedAt(LocalDateTime.now());
        mentor.setUpdatedAt(LocalDateTime.now());

        return toResponse(mentorRepository.save(mentor));
    }

    @Override
    public MentorResponse updateMentor(
            Integer mentorId,
            UpdateMentorRequest request
    ) {

        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy giảng viên hướng dẫn"
                        )
                );

        checkUpdatePermission(mentor);

        // update department
        if (request.getDepartment() != null
                && !request.getDepartment().isBlank()) {

            mentor.setDepartment(request.getDepartment());
        }

        // update academicRank
        if (request.getAcademicRank() != null
                && !request.getAcademicRank().isBlank()) {

            mentor.setAcademicRank(request.getAcademicRank());
        }

        mentor.setUpdatedAt(LocalDateTime.now());

        return toResponse(mentorRepository.save(mentor));
    }

    private MentorResponse toResponse(Mentor mentor) {

        MentorResponse response = new MentorResponse();

        response.setId(mentor.getId());
        response.setUserId(mentor.getUser().getId());
        response.setUsername(mentor.getUser().getUsername());
        response.setFullName(mentor.getUser().getFullName());
        response.setEmail(mentor.getUser().getEmail());
        response.setPhoneNumber(mentor.getUser().getPhoneNumber());
        response.setDepartment(mentor.getDepartment());
        response.setAcademicRank(mentor.getAcademicRank());

        return response;
    }
    private MentorPublicResponse toPublicResponse(
            Mentor mentor
    ) {

        MentorPublicResponse response =
                new MentorPublicResponse();

        response.setFullName(
                mentor.getUser().getFullName()
        );

        response.setPhoneNumber(
                mentor.getUser().getPhoneNumber()
        );

        response.setEmail(
                mentor.getUser().getEmail()
        );

        response.setAcademicRank(
                mentor.getAcademicRank()
        );

        response.setDepartment(
                mentor.getDepartment()
        );

        return response;
    }

    private void checkViewPermission(Mentor mentor) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User currentUser = userDetails.getUser();

        if (currentUser.getRole() == Role.MENTOR
                && !mentor.getUser().getId().equals(currentUser.getId())) {

            throw new AccessDeniedExceptionCustom(
                    "Bạn chỉ có thể xem thông tin của chính mình"
            );
        }
    }

    private void checkUpdatePermission(Mentor mentor) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User currentUser = userDetails.getUser();

        if (currentUser.getRole() == Role.MENTOR
                && !mentor.getUser().getId().equals(currentUser.getId())) {

            throw new AccessDeniedExceptionCustom(
                    "Bạn chỉ có thể cập nhật thông tin của chính mình"
            );
        }
    }
}