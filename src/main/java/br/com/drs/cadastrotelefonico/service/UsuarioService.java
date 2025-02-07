package br.com.drs.cadastrotelefonico.service;

import br.com.drs.cadastrotelefonico.model.Usuario;
import br.com.drs.cadastrotelefonico.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService extends GenericService<Usuario, Long>{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarCredenciais(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            return true;
        }
        return false;
    }

    public boolean validarSenha(String senha) {
        if (senha.length() < 8) {
            return false;
        }

        String[] senhasProibidas = {"12345678", "00000000", "11111111", "abcdefg", "password", "123456789"};
        for (String senhaFacil : senhasProibidas) {
            if (senha.equalsIgnoreCase(senhaFacil)) {
                return false;
            }
        }
        return true;
    }
}
