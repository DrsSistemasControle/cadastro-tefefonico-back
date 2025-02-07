package br.com.drs.cadastrotelefonico.controller;

import br.com.drs.cadastrotelefonico.model.Cadastro;
import br.com.drs.cadastrotelefonico.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

    @Autowired
    private CadastroService cadastroService;

    @PostMapping
    public ResponseEntity<?> salvarCadastro(@Valid @RequestBody Cadastro cadastro, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        try {
            cadastroService.salvar(cadastro);
            return ResponseEntity.ok("Cadastro realizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar o cadastro: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Cadastro> listarTodos() {
        return cadastroService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Cadastro> buscarPorId(@PathVariable Long id) {
        return cadastroService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCadastro(@PathVariable Long id, @Valid @RequestBody Cadastro cadastro, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        Optional<Cadastro> cadastroExistente = cadastroService.buscarPorId(id);
        if (!cadastroExistente.isPresent()) {
            return ResponseEntity.status(404).body("Cadastro não encontrado com o ID fornecido.");
        }

        Cadastro cadastroAtualizado = cadastroExistente.get();
        cadastroAtualizado.setNome(cadastro.getNome());
        cadastroAtualizado.setTelefone(cadastro.getTelefone());
        cadastroAtualizado.setCelular(cadastro.getCelular());
        cadastroAtualizado.setAtivo(cadastro.isAtivo());
        cadastroAtualizado.setFavorito(cadastro.isFavorito());

        cadastroService.salvar(cadastroAtualizado);

        return ResponseEntity.ok("Cadastro atualizado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public String apagar(@PathVariable Long id) {
        cadastroService.deletarPorId(id);
        return "Cadastro apagado com sucesso!";
    }

    @PostMapping("/buscar")
    public ResponseEntity<String> buscarCadastro(@RequestBody Cadastro request) {
        String resultado = cadastroService.buscarCadastro(request.getNome(), request.getCelular(), request.getTelefone());
        return ResponseEntity.ok(resultado);
    }
}