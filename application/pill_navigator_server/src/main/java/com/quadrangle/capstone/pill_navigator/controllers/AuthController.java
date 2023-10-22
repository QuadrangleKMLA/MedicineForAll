package com.quadrangle.capstone.pill_navigator.controllers;

import com.quadrangle.capstone.pill_navigator.entities.*;
import com.quadrangle.capstone.pill_navigator.exceptions.exceptionClass.TokenRefreshException;
import com.quadrangle.capstone.pill_navigator.payloads.requests.LoginRequest;
import com.quadrangle.capstone.pill_navigator.payloads.requests.RegisterRequest;
import com.quadrangle.capstone.pill_navigator.payloads.responses.MessageResponse;
import com.quadrangle.capstone.pill_navigator.payloads.responses.UserInfoResponse;
import com.quadrangle.capstone.pill_navigator.repositories.RoleRepository;
import com.quadrangle.capstone.pill_navigator.repositories.UserRepository;
import com.quadrangle.capstone.pill_navigator.security.JwtUtils;
import com.quadrangle.capstone.pill_navigator.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final SecurityContextRepository securityContextRepository;
    private final RefreshTokenService refreshTokenService;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, SecurityContextRepository securityContextRepository, RefreshTokenService refreshTokenService, SecurityContextLogoutHandler securityContextLogoutHandler) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.securityContextRepository = securityContextRepository;
        this.refreshTokenService = refreshTokenService;
        this.securityContextLogoutHandler = securityContextLogoutHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsUserByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("Username is already taken"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(registerRequest.getUsername(), encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role role = roleRepository.findRoleByRole(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role doesn't exist."));
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("MANAGER")) {
                    Role admin = roleRepository.findRoleByRole(ERole.ROLE_MANAGER)
                            .orElseThrow(() -> new RuntimeException("Error: Role doesn't exist."));

                    roles.add(admin);
                } else {
                    Role defaultRole = roleRepository.findRoleByRole(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role doesn't exist."));

                    roles.add(defaultRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("User registered successfully!"), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@CurrentSecurityContext(expression = "authentication") Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String username = userDetails.getUsername();
            refreshTokenService.deleteByUsername(username);
        }
        securityContextLogoutHandler.logout(request, response, authentication);

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You've been logged out!"));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<MessageResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token refreshed successfully"));
            })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database."));
        }

        return new ResponseEntity<>(new MessageResponse("Refresh token is empty"), HttpStatus.BAD_REQUEST);
    }
}
