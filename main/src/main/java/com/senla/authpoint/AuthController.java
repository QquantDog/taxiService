package com.senla.authpoint;


import com.senla.dto.user.UserResponseWithRoleDto;
import com.senla.dto.user.auth.LoginResponseDto;
import com.senla.dto.user.auth.LoginUserDto;
import com.senla.dto.user.auth.RegisterResponseDto;
import com.senla.dto.user.auth.RegisterUserDto;
import com.senla.model.user.User;
import com.senla.util.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ModelMapper mapper;


    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        User registeredUser = authService.signup(registerUserDto);
        String jwtToken = jwtUtils.generateSimpleJwt(registeredUser.getEmail(), registeredUser.getUserId());

        return ResponseEntity.ok(convertToRegisterResponseDto(registeredUser, jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody @Valid LoginUserDto loginUserDto) {
        User authenticatedUser = authService.login(loginUserDto);
        String jwtToken = jwtUtils.generateSimpleJwt(authenticatedUser.getEmail(), authenticatedUser.getUserId());

        return ResponseEntity.ok(LoginResponseDto.builder().token(jwtToken).build());
    }

    private RegisterResponseDto convertToRegisterResponseDto(User user, String jwtToken){
        return RegisterResponseDto.builder()
                    .user(mapper.map(user, UserResponseWithRoleDto.class))
                    .token(jwtToken)
                    .build();
    }

}
