package re.edu.service;

import re.edu.model.dto.request.authReq.UpdatePasswordRequest;
import re.edu.model.dto.request.authReq.UserCreateDTO;
import re.edu.model.dto.request.userReq.UpdateUserRequest;
import re.edu.model.dto.response.userRes.UserResponse;
import re.edu.model.enums.Role;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers(Role role);

    UserResponse getUserById(Integer userId);

    UserResponse updateUser(
            Integer userId,
            UpdateUserRequest req
    );

    String updateUserPassword(
            Integer userId,
            UpdatePasswordRequest req
    );

    String updateUserStatus(
            Integer userId,
            Boolean status
    );

    String updateUserRole(
            Integer userId,
            Role role
    );

    String deleteUser(
            Integer userId
    );
    UserResponse createUser(UserCreateDTO req);
}