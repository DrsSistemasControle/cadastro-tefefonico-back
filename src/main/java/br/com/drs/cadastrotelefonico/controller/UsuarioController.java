package br.com.drs.cadastrotelefonico.controller;

import br.com.drs.cadastrotelefonico.model.Cadastro;
import br.com.drs.cadastrotelefonico.model.Usuario;
import br.com.drs.cadastrotelefonico.service.CadastroService;
import br.com.drs.cadastrotelefonico.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> salvarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        if (!usuarioService.validarSenha(usuario.getSenha())) {
            return ResponseEntity.badRequest().body("A senha fornecida é fraca ou muito comum.");
        }

        try {
            usuarioService.salvar(usuario);
            return ResponseEntity.ok("Cadastro realizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar o usuário: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        Optional<Usuario> usuarioExistente = usuarioService.buscarPorId(id);
        if (!usuarioExistente.isPresent()) {
            return ResponseEntity.status(404).body("Usuário não encontrado com o ID fornecido.");
        }

        if (!usuarioService.validarSenha(usuario.getSenha())) {
            return ResponseEntity.badRequest().body("A senha fornecida é fraca ou muito comum.");
        }

        Usuario usuarioAtualizado = usuarioExistente.get();
        usuarioAtualizado.setEmail(usuario.getEmail());
        usuarioAtualizado.setSenha(usuario.getSenha());

        usuarioService.salvar(usuarioAtualizado);

        return ResponseEntity.ok("Usuário atualizado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public String apagar(@PathVariable Long id) {
        usuarioService.deletarPorId(id);
        return "Usuário apagado com sucesso!";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario loginRequest) {
        String email = loginRequest.getEmail();
        String senha = loginRequest.getSenha();

        boolean credenciaisValidas = usuarioService.validarCredenciais(email, senha);

        if (credenciaisValidas) {
            return ResponseEntity.ok("Login bem-sucedido");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }
}