package re.edu.model.dto.response.userRes;

import re.edu.model.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
    private String fullName;
    private Role role;
    private Boolean isActive;
}
