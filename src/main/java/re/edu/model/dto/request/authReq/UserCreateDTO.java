package re.edu.model.dto.request.authReq;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import re.edu.model.enums.Role;

@Getter
@Setter
public class UserCreateDTO {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 50, message = "Username phải từ 4 đến 50 ký tự")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username chỉ được chứa chữ, số, dấu chấm, gạch dưới hoặc gạch ngang"
    )
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, max = 100, message = "Password phải từ 6 đến 100 ký tự")
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
    private String fullName;

    @NotNull(message = "Role không được để trống")
    private Role role;
}