package br.com.drs.cadastrotelefonico.repository;

import br.com.drs.cadastrotelefonico.model.Cadastro;

import java.util.Optional;

public interface CadastroRepository extends GenericRepository<Cadastro, Long> {

    Optional<Cadastro> findByCelular(String celular);

    boolean existsByCelular(String celular);

    Optional<Cadastro> findByNome(String nome);

    Optional<Cadastro> findByTelefone(String telefone);
}
