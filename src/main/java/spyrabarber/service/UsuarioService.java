package spyrabarber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spyrabarber.domain.Perfil;
import spyrabarber.domain.Usuario;
import spyrabarber.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepo;

    @Override @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = buscarPorEmailEAtivo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " não encontrado/ativo!"));
        return new User(
                user.getEmail(),
                user.getSenha(),
                AuthorityUtils.createAuthorityList(getAuthoritys(user.getPerfis()))
        );
    }

    private String[] getAuthoritys(List<Perfil> perfis){
        String[] arr = new String[perfis.size()];
        for(int i = 0; i < perfis.size(); i++){
            arr[i] = perfis.get(i).getDesc();
        }
        return arr;
    }

    @Transactional(readOnly = true)
    private Optional<Usuario> buscarPorEmailEAtivo(String username) {
        return userRepo.findByEmailAndAtivo(username);
    }
}
