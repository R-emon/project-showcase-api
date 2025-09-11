package com.showcase.api.controller;

import com.showcase.api.dto.JwtResponse;
import com.showcase.api.dto.LoginRequest;
import com.showcase.api.dto.RegisterRequest;
import com.showcase.api.model.User;
import com.showcase.api.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public  AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest){

        if(userRepository.findByUsername(registerRequest.username()).isPresent()){
            return  ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());

        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(("User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    private String generateJwtToken(Authentication authentication){
        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .compact();

    }

}
