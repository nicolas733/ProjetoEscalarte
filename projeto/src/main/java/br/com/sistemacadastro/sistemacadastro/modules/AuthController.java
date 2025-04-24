package br.com.sistemacadastro.sistemacadastro.modules;


import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.LoginRequestDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.ResponseDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CollaboratorRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto body){
        Collaborator collaborator = this.repository.findByEmail(body.email()).orElseThrow(()-> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.senha(), collaborator.getSenha())){
            String token = this.tokenService.generateToken(collaborator);
            return ResponseEntity.ok(new ResponseDto(collaborator.getEmail(), token));
        }else
            return ResponseEntity.badRequest().build();

    }
}
