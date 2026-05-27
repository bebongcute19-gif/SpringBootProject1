package re.edu.model.dto.response.mentorRes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorPublicResponse {

    private String fullName;

    private String phoneNumber;

    private String email;

    private String academicRank;

    private String department;
}