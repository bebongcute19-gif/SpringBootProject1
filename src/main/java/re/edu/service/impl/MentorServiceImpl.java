package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.mentorReq.MentorRequest;
import re.edu.model.dto.response.mentorRes.MentorResponse;
import re.edu.model.entity.Mentor;
import re.edu.model.enums.Role;
import re.edu.model.entity.User;
import re.edu.repository.userRep.MentorRepository;
import re.edu.repository.userRep.UserRepository;
import re.edu.security.userDetail.CustomUserDetails;
import re.edu.service.MentorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;

    @Override
    public List<MentorResponse> getAllMentors() {

        List<Mentor> mentors = mentorRepository.findAll();

        return mentors.stream().map(this::toResponse).toList();
    }

    @Override
    public MentorResponse getMentorById(Integer mentorId) {

        Mentor mentor = mentorRepository.findById(mentorId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên hướng dẫn"));

        checkViewPermission(mentor);

        return toResponse(mentor);
    }

    @Override
    public MentorResponse createMentor(MentorRequest request) {

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (user.getRole() != Role.MENTOR) {

            throw new RuntimeException("Vai trò của người dùng phải là MENTOR");
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
    public MentorResponse updateMentor(Integer mentorId, MentorRequest request) {

        Mentor mentor = mentorRepository.findById(mentorId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên hướng dẫn"));

        checkUpdatePermission(mentor);

        if (request.getDepartment() != null) {
            mentor.setDepartment(request.getDepartment());
        }

        if (request.getAcademicRank() != null) {
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

    private void checkViewPermission(Mentor mentor) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userDetails.getUser();

        if (currentUser.getRole() == Role.MENTOR && !mentor.getUser().getId().equals(currentUser.getId())) {

            throw new AccessDeniedExceptionCustom("Bạn chỉ có thể xem thông tin của chính mình");
        }
    }

    private void checkUpdatePermission(Mentor mentor) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userDetails.getUser();

        if (currentUser.getRole() == Role.MENTOR && !mentor.getUser().getId().equals(currentUser.getId())) {

            throw new AccessDeniedExceptionCustom("Bạn chỉ có thể cập nhật thông tin của chính mình");
        }
    }
}