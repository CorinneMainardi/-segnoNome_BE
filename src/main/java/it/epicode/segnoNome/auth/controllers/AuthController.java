package it.epicode.segnoNome.auth.controllers;


import it.epicode.segnoNome.auth.dto.requests.LoginRequest;
import it.epicode.segnoNome.auth.dto.requests.RegisterRequest;
import it.epicode.segnoNome.auth.dto.responses.AuthResponse;
import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.auth.enums.Role;
import it.epicode.segnoNome.auth.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AppUserService appUserService;
    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = appUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

        @GetMapping("/me")
        public ResponseEntity<AppUser> getCurrentUser(@AuthenticationPrincipal AppUser user) {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(user);
        }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                roles
        );

        return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione avvenuta con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));

    }
}
