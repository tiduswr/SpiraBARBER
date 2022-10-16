package spyrabarber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spyrabarber.domain.*;
import spyrabarber.repository.CargoHistoricoRepository;
import spyrabarber.repository.PessoaRepository;
import spyrabarber.repository.UserCargoRepository;
import spyrabarber.repository.UsuarioRepository;
import spyrabarber.web.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepo;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private CargoHistoricoRepository cargoHistoricoRepository;

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

    private String[] getAuthoritys(Set<Perfil> perfis){
        List<Perfil> p = new ArrayList<>(perfis);

        String[] arr = new String[p.size()];
        for(int i = 0; i < p.size(); i++){
            arr[i] = p.get(i).getDesc();
        }

        return arr;
    }

    @Transactional(readOnly = true)
    private Optional<Usuario> buscarPorEmailEAtivo(String username) {
        return userRepo.findByEmailAndAtivo(username);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodosOsUsuarios(){
        return userRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) throws UsuarioNotFoundException{
        return userRepo.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario não encontrado na base de dados do servidor"));
    }

    @Transactional(readOnly = false)
    public void saveUser(Usuario user) throws ClienteHasMoreThanOnePerfilException, UserHasNotDataToBeActiveException{
        Usuario bd_user = userRepo.findById(user.getId()).orElse(null);
        if(bd_user != null && bd_user.getServicos() != null && !bd_user.getServicos().isEmpty())
            user.setServicos(bd_user.getServicos());

        if(user.getPerfis().contains(PerfilTipo.CLIENTE.buildPerfil()) && user.getPerfis().size() > 1){
            throw new ClienteHasMoreThanOnePerfilException("O cliente não pode ter mais de um perfil");
        }

        user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
        user = userRepo.save(user);

        if(user.isAtivo()){
            Optional<Pessoa> pessoa = pessoaRepository.findByUserId(user.getId());
            if(pessoa.isEmpty()) throw new UserHasNotDataToBeActiveException("O usuário não possui cadastro completo para estar ativo");
        }
    }

    @Transactional(readOnly = false)
    public void deleteUser(Long id, User user) throws SelfExclusionException, HasCargosException, UsuarioNotFoundException{
        Usuario usuario = userRepo.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario não encontrado na base de dados do servidor"));
        Pessoa pessoa = pessoaRepository.findByUserId(id).orElse(new Pessoa());

        checkIfHasDependency(user, usuario);
        if(pessoa.getId() != null) pessoaRepository.delete(pessoa);
        userRepo.delete(usuario);
    }

    private void checkIfHasDependency(User user, Usuario usuario){
        if(user.getUsername().equalsIgnoreCase(usuario.getEmail())) {
            throw new SelfExclusionException("Você não pode se excluir");
        }else if(!cargoHistoricoRepository.findAllByUserId(usuario.getId()).isEmpty()){
            throw new HasCargosException("Esse perfil possui histórico de cargos, tente apenas inativalo");
        }else if(!usuario.getServicos().isEmpty()){
            throw new HasServicosException("Esse perfil possui serviços vinculados, tente remove-los antes de excluir");
        }
    }

}
