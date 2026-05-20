package re.edu.model.dto.response.authRes;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import re.edu.model.enums.Role;

@Getter
@Setter
@Builder
public class VerifyTokenResponse {
    private Boolean valid;
    private String username;
    private Role role;
}
