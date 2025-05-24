// src/main/java/grupo5/gestion_inventario/controller/AuthController.java
package grupo5.gestion_inventario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import grupo5.gestion_inventario.config.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil     = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> creds) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.get("username"),
                        creds.get("password")
                )
        );
        String token = jwtUtil.generateToken(auth.getName());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
