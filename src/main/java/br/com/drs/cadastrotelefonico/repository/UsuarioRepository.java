package br.com.drs.cadastrotelefonico.repository;

import br.com.drs.cadastrotelefonico.model.Cadastro;
import br.com.drs.cadastrotelefonico.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends GenericRepository<Usuario, Long>{

    Optional<Usuario> findByEmail(String email);
}
