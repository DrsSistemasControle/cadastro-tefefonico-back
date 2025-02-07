package br.com.drs.cadastrotelefonico.service;

import br.com.drs.cadastrotelefonico.model.Usuario;
import br.com.drs.cadastrotelefonico.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("senhaForte123");
    }

    @Test
    void testValidarCredenciais_CredenciaisCorretas() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        boolean resultado = usuarioService.validarCredenciais("teste@teste.com", "senhaForte123");

        assertTrue(resultado);
    }

    @Test
    void testValidarCredenciais_EmailNaoEncontrado() {
        when(usuarioRepository.findByEmail("inexistente@teste.com")).thenReturn(Optional.empty());

        boolean resultado = usuarioService.validarCredenciais("inexistente@teste.com", "senhaErrada");

        assertFalse(resultado);
    }

    @Test
    void testValidarCredenciais_SenhaIncorreta() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        boolean resultado = usuarioService.validarCredenciais("teste@teste.com", "senhaErrada");

        assertFalse(resultado);
    }

    @Test
    void testValidarSenha_SenhaForte() {
        assertTrue(usuarioService.validarSenha("SenhaSegura123!"));
    }

    @Test
    void testValidarSenha_SenhaCurta() {
        assertFalse(usuarioService.validarSenha("1234567"));
    }

    @Test
    void testValidarSenha_SenhaProibida() {
        assertFalse(usuarioService.validarSenha("12345678"));
        assertFalse(usuarioService.validarSenha("password"));
    }
}

