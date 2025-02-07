package br.com.drs.cadastrotelefonico.service;

import br.com.drs.cadastrotelefonico.model.Cadastro;
import br.com.drs.cadastrotelefonico.repository.CadastroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastroServiceTest {

    @Mock
    private CadastroRepository cadastroRepository;

    @InjectMocks
    private CadastroService cadastroService;

    private Cadastro cadastroAtivo;
    private Cadastro cadastroInativo;

    @BeforeEach
    void setUp() {
        cadastroAtivo = new Cadastro();
        cadastroAtivo.setNome("João Silva");
        cadastroAtivo.setCpf("123.456.789-00");
        cadastroAtivo.setCelular("11987654321");
        cadastroAtivo.setTelefone("1133334444");
        cadastroAtivo.setAtivo(true);
        cadastroAtivo.setFavorito(true);

        cadastroInativo = new Cadastro();
        cadastroInativo.setNome("Maria Souza");
        cadastroInativo.setCpf("987.654.321-00");
        cadastroInativo.setCelular("11912345678");
        cadastroInativo.setTelefone("1122223333");
        cadastroInativo.setAtivo(false);
        cadastroInativo.setFavorito(false);
    }

    @Test
    void testBuscarCadastroPorNome_EncontradoAtivo() {
        when(cadastroRepository.findByNome("João Silva")).thenReturn(Optional.of(cadastroAtivo));

        String resultado = cadastroService.buscarCadastro("João Silva", null, null);

        String esperado = "Cadastro encontrado:\n" +
                "Nome: João Silva\n" +
                "CPF: 123.456.789-00\n" +
                "Celular: 11987654321\n" +
                "Telefone: 1133334444\n" +
                "Ativo: true\n" +
                "O cadastro é favorito.";

        assertEquals(esperado, resultado);
    }

    @Test
    void testBuscarCadastroPorNome_EncontradoInativo() {
        when(cadastroRepository.findByNome("Maria Souza")).thenReturn(Optional.of(cadastroInativo));

        String resultado = cadastroService.buscarCadastro("Maria Souza", null, null);

        assertEquals("Cadastro inativo.", resultado);
    }

    @Test
    void testBuscarCadastroPorCelular_EncontradoAtivo() {
        when(cadastroRepository.findByCelular("11987654321")).thenReturn(Optional.of(cadastroAtivo));

        String resultado = cadastroService.buscarCadastro(null, "11987654321", null);

        assertEquals("Cadastro encontrado:\n" +
                "Nome: João Silva\n" +
                "CPF: 123.456.789-00\n" +
                "Celular: 11987654321\n" +
                "Telefone: 1133334444\n" +
                "Ativo: true\n" +
                "O cadastro é favorito.", resultado);
    }

    @Test
    void testBuscarCadastroPorTelefone_NaoEncontrado() {
        when(cadastroRepository.findByTelefone("1144445555")).thenReturn(Optional.empty());

        String resultado = cadastroService.buscarCadastro(null, null, "1144445555");

        assertEquals("Nenhum cadastro encontrado.", resultado);
    }
}