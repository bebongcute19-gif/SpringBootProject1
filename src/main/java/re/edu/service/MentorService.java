package re.edu.service;

import re.edu.model.dto.request.mentorReq.MentorRequest;
import re.edu.model.dto.response.mentorRes.MentorResponse;

import java.util.List;

public interface MentorService {

    List<MentorResponse> getAllMentors();

    MentorResponse getMentorById(Integer mentorId);

    MentorResponse createMentor(MentorRequest request);

    MentorResponse updateMentor(Integer mentorId, MentorRequest request);
}