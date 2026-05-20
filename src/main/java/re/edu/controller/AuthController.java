package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.authReq.UserLoginDTO;
import re.edu.model.dto.request.authReq.VerifyTokenRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * LOGIN
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse>
    login(
            @Valid
            @RequestBody
            UserLoginDTO req
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        authService.login(req),
                        null,
                        200,
                        "Đăng nhập thành công"
                )
        );
    }

    /**
     * VERIFY TOKEN
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse>
    verify(
            @RequestBody
            VerifyTokenRequest req
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        authService.verifyToken(req),
                        null,
                        200,
                        "Xác thực token thành công"
                )
        );
    }

    /**
     * CURRENT USER
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse>
    me() {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        authService.getCurrentUser(),
                        null,
                        200,
                        "Lấy thông tin người dùng hiện tại thành công"
                )
        );
    }
}