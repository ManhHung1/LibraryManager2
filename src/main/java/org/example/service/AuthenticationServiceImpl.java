package org.example.service;

import lombok.AllArgsConstructor;
import org.example.interfaces.AuthenticationService;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.jwt.JwtUtil;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        // Load the user from the database
        User user = userRepository.findByUsername(authRequest.getUsername());

        // Generate JWT token including the user's role
        final String jwt = jwtUtil.generateToken(user);

        return new AuthResponse(jwt);
    }
}
