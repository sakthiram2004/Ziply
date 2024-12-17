package com.sr.Ziply.controller.authentication;


import com.sr.Ziply.model.User;
import com.sr.Ziply.request.LoginRequest;
import com.sr.Ziply.response.ApiResponse;
import com.sr.Ziply.response.LoginResponse;
import com.sr.Ziply.request.RegisterRequest;
import com.sr.Ziply.service.JwtUtils;
import com.sr.Ziply.service.OurUserDetailService;
import com.sr.Ziply.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
        private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final OurUserDetailService userDetailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user= userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User Registered Successfully",userService.convertUserDto(user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            final UserDetails userDetails = userDetailService.loadUserByUsername(loginRequest.getEmail());
            final String jwt = jwtUtils.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
