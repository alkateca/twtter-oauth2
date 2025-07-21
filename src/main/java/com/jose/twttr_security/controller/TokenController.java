package com.jose.twttr_security.controller;

import com.jose.twttr_security.controller.dto.LoginRequest;
import com.jose.twttr_security.controller.dto.LoginResponse;
import com.jose.twttr_security.entities.Role;
import com.jose.twttr_security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class TokenController {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
       var user = userRepository.findByUsername(loginRequest.username());
       if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)){
           throw new BadCredentialsException("Invalid username or password");
       }

       var expiresIn = 300L;
       var now = Instant.now();

       var scopes = user.get().getRoles()
               .stream()
               .map(Role::getName)
               .collect(Collectors.joining(" "));

       var claims = JwtClaimsSet.builder()
               .issuer("myBackend")
               .subject(user.get().getUserId().toString())
               .issuedAt(now)
               .expiresAt(now.plusSeconds(expiresIn))
               .claim("scope", scopes)
               .build();

       var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

       return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));

    }



}
