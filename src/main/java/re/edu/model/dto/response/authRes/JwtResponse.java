package re.edu.model.dto.response.authRes;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import re.edu.model.enums.Role;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private final String type = "Bearer";
    private String username;
    private Role role;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private Date expirationDate;
}
