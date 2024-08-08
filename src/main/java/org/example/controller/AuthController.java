package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.config.ResponseConfig;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.ResponseDto;
import org.example.interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user and generate JWT",
            description = "Authenticates the user with the provided credentials and generates a JWT token if the credentials are valid."
    )
    public ResponseEntity<ResponseDto<AuthResponse>> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authenticationService.authenticate(authRequest);
        return ResponseConfig.success(authResponse);
    }
}
