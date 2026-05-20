package re.edu.model.dto.request.authReq;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyTokenRequest {
    @NotBlank(message = "Token không được để trống")
    private String token;
}