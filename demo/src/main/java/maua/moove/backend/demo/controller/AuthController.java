package maua.moove.backend.demo.controller;

import maua.moove.backend.demo.dto.JwtResponse;
import maua.moove.backend.demo.dto.LoginRequest;
import maua.moove.backend.demo.dto.MessageResponse;
import maua.moove.backend.demo.dto.RegisterRequest;
import maua.moove.backend.demo.model.TipoUsuarioEnum;
import maua.moove.backend.demo.model.User;
import maua.moove.backend.demo.repository.UserRepository;
import maua.moove.backend.demo.security.JwtTokenUtil;
import maua.moove.backend.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String jwt = jwtTokenUtil.generateToken(userDetails);
            
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

            return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erro: Email ou senha inválidos!"));
        }
    }

    @PostMapping("/register")
        public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Erro: Email já está em uso!"));
            }

            // Verificar se as senhas coincidem
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Erro: As senhas não coincidem!"));
            }

            // Criar nova conta de usuário
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            

            user.setName(registerRequest.getEmail().split("@")[0]);
            
            user.setUsername(registerRequest.getEmail());
            user.setActive(true);

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Usuário registrado com sucesso!"));
        }
}