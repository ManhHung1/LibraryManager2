package org.example.interfaces;

import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {
    AuthResponse authenticate(AuthRequest authRequest) throws AuthenticationException;

}