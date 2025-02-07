package br.com.drs.cadastrotelefonico.service;

import br.com.drs.cadastrotelefonico.model.Cadastro;
import br.com.drs.cadastrotelefonico.repository.CadastroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CadastroService extends GenericService<Cadastro, Long>{

    @Autowired
    private CadastroRepository cadastroRepository;

    public String buscarCadastro(String nome, String celular, String telefone) {
        Optional<Cadastro> cadastroOptional = Optional.empty();

        if (nome != null && !nome.isEmpty()) {
            cadastroOptional = cadastroRepository.findByNome(nome);
        } else if (celular != null && !celular.isEmpty()) {
            cadastroOptional = cadastroRepository.findByCelular(celular);
        } else if (telefone != null && !telefone.isEmpty()) {
            cadastroOptional = cadastroRepository.findByTelefone(telefone);
        }

        // Se não encontrar o cadastro
        if (!cadastroOptional.isPresent()) {
            return "Nenhum cadastro encontrado.";
        }

        Cadastro cadastro = cadastroOptional.get();

        // Se o cadastro estiver inativo
        if (!cadastro.isAtivo()) {
            return "Cadastro inativo.";
        }

        // Se o cadastro estiver ativo, mostrar seus dados
        String favoritoMsg = cadastro.isFavorito() ? "O cadastro é favorito." : "O cadastro não é favorito.";

        return "Cadastro encontrado:\n" +
                "Nome: " + cadastro.getNome() + "\n" +
                "CPF: " + cadastro.getCpf() + "\n" +
                "Celular: " + cadastro.getCelular() + "\n" +
                "Telefone: " + cadastro.getTelefone() + "\n" +
                "Ativo: " + cadastro.isAtivo() + "\n" +
                favoritoMsg;
    }
}