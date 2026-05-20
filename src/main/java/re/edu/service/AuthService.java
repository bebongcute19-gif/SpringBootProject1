package re.edu.service;


import re.edu.model.dto.request.authReq.UserCreateDTO;
import re.edu.model.dto.request.authReq.UserLoginDTO;
import re.edu.model.dto.request.authReq.VerifyTokenRequest;
import re.edu.model.dto.response.authRes.JwtResponse;
import re.edu.model.dto.response.authRes.VerifyTokenResponse;
import re.edu.model.dto.response.userRes.UserResponse;

public interface AuthService {

    UserResponse register(UserCreateDTO req);

    JwtResponse login(UserLoginDTO req);

    VerifyTokenResponse verifyToken(VerifyTokenRequest req);

    UserResponse getCurrentUser();
}