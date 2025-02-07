package br.com.drs.cadastrotelefonico.controller;

import br.com.drs.cadastrotelefonico.model.Usuario;
import br.com.drs.cadastrotelefonico.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private BindingResult bindingResult;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setSenha("StrongPassword123");
    }

    @Test
    public void testSalvarUsuario_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.validarSenha(usuario.getSenha())).thenReturn(true);
        when(usuarioService.salvar(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.salvarUsuario(usuario, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cadastro realizado com sucesso.", response.getBody());
    }

    @Test
    public void testSalvarUsuario_BadRequest_SenhaFraca() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.validarSenha(usuario.getSenha())).thenReturn(false);

        ResponseEntity<?> response = usuarioController.salvarUsuario(usuario, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("A senha fornecida é fraca ou muito comum.", response.getBody());
    }

    @Test
    public void testListarTodos() {
        usuarioController.listarTodos();
        verify(usuarioService, times(1)).listarTodos();
    }

    @Test
    public void testBuscarPorId() {
        Long id = 1L;
        when(usuarioService.buscarPorId(id)).thenReturn(Optional.of(usuario));

        Optional<Usuario> response = usuarioController.buscarPorId(id);

        assertTrue(response.isPresent());
        assertEquals(usuario, response.get());
    }

    @Test
    public void testAtualizarUsuario_Success() {
        Long id = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.buscarPorId(id)).thenReturn(Optional.of(usuario));
        when(usuarioService.validarSenha(usuario.getSenha())).thenReturn(true);
        when(usuarioService.salvar(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.atualizarUsuario(id, usuario, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário atualizado com sucesso.", response.getBody());
    }

    @Test
    public void testApagarUsuario() {
        Long id = 1L;
        usuarioController.apagar(id);
        verify(usuarioService, times(1)).deletarPorId(id);
    }

    @Test
    public void testLogin_Success() {
        when(usuarioService.validarCredenciais(usuario.getEmail(), usuario.getSenha())).thenReturn(true);

        ResponseEntity<String> response = usuarioController.login(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login bem-sucedido", response.getBody());
    }

    @Test
    public void testLogin_Failure() {
        when(usuarioService.validarCredenciais(usuario.getEmail(), usuario.getSenha())).thenReturn(false);

        ResponseEntity<String> response = usuarioController.login(usuario);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciais inválidas", response.getBody());
    }
}