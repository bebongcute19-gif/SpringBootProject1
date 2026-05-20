package re.edu.model.dto.response.mentorRes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorResponse {

    private Integer id;

    private Integer userId;

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String department;

    private String academicRank;
}