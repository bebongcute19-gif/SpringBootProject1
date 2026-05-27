package re.edu.service;

import re.edu.model.dto.request.mentorReq.MentorRequest;
import re.edu.model.dto.request.mentorReq.UpdateMentorRequest;
import re.edu.model.dto.response.mentorRes.MentorResponse;

import java.util.List;

public interface MentorService {
    boolean existsByMentor_Id(Integer mentorId);

    List<?> getAllMentors();

    MentorResponse getMentorById(Integer mentorId);

    MentorResponse createMentor(MentorRequest request);

    public MentorResponse updateMentor(Integer mentorId, UpdateMentorRequest request);
}