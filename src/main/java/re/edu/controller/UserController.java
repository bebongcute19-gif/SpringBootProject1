package re.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.edu.mapper.MapToAPIResponse;
import re.edu.model.dto.request.authReq.UpdatePasswordRequest;
import re.edu.model.dto.request.authReq.UserCreateDTO;
import re.edu.model.dto.request.userReq.UpdateUserRequest;
import re.edu.model.dto.response.authRes.ApiResponse;
import re.edu.model.enums.Role;
import re.edu.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET ALL USERS
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam(required = false) Role role) {

        return ResponseEntity.ok(MapToAPIResponse.mapTo(userService.getAllUsers(role), null, 200, "Lấy danh sách người dùng thành công"));
    }

    /**
     * GET USER BY ID
     * GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Integer userId) {

        return ResponseEntity.ok(MapToAPIResponse.mapTo(userService.getUserById(userId), null, 200, "Lấy thông tin người dùng thành công"));
    }

    /**
     * CREATE USER
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserCreateDTO req) {

        return ResponseEntity.status(HttpStatus.CREATED).body(MapToAPIResponse.mapTo(userService.createUser(req), null, 201, "Tạo người dùng thành công"));
    }

    /**
     * UPDATE USER
     * PUT /api/users/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer userId, @Valid @RequestBody UpdateUserRequest req) {

        return ResponseEntity.ok(MapToAPIResponse.mapTo(userService.updateUser(userId, req), null, 200, "Cập nhật người dùng thành công"));
    }

    /**
     * UPDATE PASSWORD
     * PUT /api/users/{userId}/password
     */
    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponse> updatePassword(@PathVariable Integer userId, @RequestBody UpdatePasswordRequest req) {

        return ResponseEntity.ok(MapToAPIResponse.mapTo(userService.updateUserPassword(userId, req), null, 200, "Cập nhật mật khẩu thành công"));
    }

    /**
     * UPDATE STATUS
     * PUT /api/users/{userId}/status
     */
    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Integer userId, @RequestParam Boolean status) {

        return ResponseEntity.ok(MapToAPIResponse.mapTo(userService.updateUserStatus(userId, status), null, 200, "Cập nhật trạng thái người dùng thành công"));
    }

    /**
     * UPDATE ROLE
     * PUT /api/users/{userId}/role
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Integer userId, @RequestParam Role role) {

        return ResponseEntity.ok(MapToAPIResponse.mapTo(userService.updateUserRole(userId, role), null, 200, "Cập nhật vai trò người dùng thành công"));
    }

    /**
     * DELETE USER
     * DELETE /api/users/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId) {

        return ResponseEntity.ok(MapToAPIResponse.mapTo(userService.deleteUser(userId), null, 200, "Xóa người dùng thành công"));
    }
}