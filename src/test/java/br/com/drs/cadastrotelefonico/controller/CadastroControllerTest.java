package br.com.drs.cadastrotelefonico.controller;

import br.com.drs.cadastrotelefonico.model.Cadastro;
import br.com.drs.cadastrotelefonico.service.CadastroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CadastroControllerTest {

    @InjectMocks
    private CadastroController cadastroController;

    @Mock
    private CadastroService cadastroService;

    @Mock
    private BindingResult bindingResult;

    private Cadastro cadastro;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cadastro = new Cadastro();
        cadastro.setNome("Teste");
        cadastro.setTelefone("123456789");
        cadastro.setCelular("987654321");
        cadastro.setAtivo(true);
        cadastro.setFavorito(false);
    }

    @Test
    public void testSalvarCadastro_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cadastroService.salvar(any(Cadastro.class))).thenReturn(cadastro);

        ResponseEntity<?> response = cadastroController.salvarCadastro(cadastro, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cadastro realizado com sucesso.", response.getBody());
    }

    @Test
    public void testSalvarCadastro_BadRequest() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("nome", "Nome é obrigatório")));

        ResponseEntity<?> response = cadastroController.salvarCadastro(cadastro, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Nome é obrigatório"));
    }

    @Test
    public void testListarTodos() {
        cadastroController.listarTodos();
        verify(cadastroService, times(1)).listarTodos();
    }

    @Test
    public void testBuscarPorId() {
        Long id = 1L;
        when(cadastroService.buscarPorId(id)).thenReturn(Optional.of(cadastro));

        Optional<Cadastro> response = cadastroController.buscarPorId(id);

        assertTrue(response.isPresent());
        assertEquals(cadastro, response.get());
    }

    @Test
    public void testAtualizarCadastro_Success() {
        Long id = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cadastroService.buscarPorId(id)).thenReturn(Optional.of(cadastro));
        when(cadastroService.salvar(any(Cadastro.class))).thenReturn(cadastro);

        ResponseEntity<?> response = cadastroController.atualizarCadastro(id, cadastro, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cadastro atualizado com sucesso.", response.getBody());
    }

    @Test
    public void testAtualizarCadastro_NotFound() {
        Long id = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cadastroService.buscarPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = cadastroController.atualizarCadastro(id, cadastro, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cadastro não encontrado com o ID fornecido.", response.getBody());
    }

    @Test
    public void testApagarCadastro() {
        Long id = 1L;
        cadastroController.apagar(id);
        verify(cadastroService, times(1)).deletarPorId(id);
    }

    @Test
    public void testBuscarCadastro() {
        String nome = "Teste";
        String celular = "987654321";
        String telefone = "123456789";
        when(cadastroService.buscarCadastro(nome, celular, telefone)).thenReturn("Resultado da busca");

        ResponseEntity<String> response = cadastroController.buscarCadastro(cadastro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Resultado da busca", response.getBody());
    }
}