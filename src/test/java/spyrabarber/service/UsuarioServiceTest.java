package spyrabarber.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import config.ApplicationConfigTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import spyrabarber.domain.*;
import spyrabarber.repository.CargoHistoricoRepository;
import spyrabarber.repository.PessoaRepository;
import spyrabarber.repository.UserCargoRepository;
import spyrabarber.repository.UsuarioRepository;
import spyrabarber.web.exception.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Testando Usuario service")
public class UsuarioServiceTest extends ApplicationConfigTest {

    @MockBean
    private UsuarioRepository userRepo;

    @MockBean
    private CargoHistoricoRepository cargoHistoricoRepository;

    @MockBean
    private PessoaRepository pessoaRepository;

    @Autowired
    private UsuarioService userService;

    @Test
    public void shouldFindUserByUsernameAndActive(){
        String mockedUsername = "mockuser@gmail.com";
        Optional<Usuario> mockedUser = Optional.of(createMockedUser(mockedUsername));

        //Mocka o metodo findByEmailAndAtivo
        Mockito.when(userRepo.findByEmailAndAtivo(ArgumentMatchers.eq(mockedUsername)))
                .thenReturn(mockedUser);

        //Testa o método
        userService.loadUserByUsername(mockedUsername);

        //Verifica se o metodo findByEmailAndAtivo foi chamado 1 vez
        Mockito.verify(userRepo, Mockito.times(1)).findByEmailAndAtivo(ArgumentMatchers.any(String.class));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException(){
        //Testa o método
        userService.loadUserByUsername(ArgumentMatchers.any(String.class));
    }

    @Test
    public void shouldFindById(){
        final long mockedID = 1L;
        Optional<Usuario> mockedUser = Optional.of(createMockedUserById(mockedID));

        //Mocka repositorio
        Mockito.when(userRepo.findById(ArgumentMatchers.eq(mockedID))).thenReturn(mockedUser);

        Usuario user = userService.buscarPorId(mockedID);

        assertThat(user.getId()).isEqualTo(mockedID);
    }

    @Test(expected = UsuarioNotFoundException.class)
    public void shouldThrowUsuarioNotFoundException(){
        final long mockedID = 1L;
        Usuario user = userService.buscarPorId(mockedID);
        Mockito.verify(userRepo, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
    }

    @Test
    public void shouldSaveUser(){
        Usuario testUser = new Usuario();

        testUser.setAtivo(false);
        testUser.addPerfil(PerfilTipo.ADMIN);

        setupSaveTest(testUser);
    }

    @Test(expected = UserHasNotDataToBeActiveException.class)
    public void shouldBlockSaveActionBecauseUserDataNotComplete(){
        Usuario testUser = new Usuario();

        testUser.setAtivo(true);
        testUser.addPerfil(PerfilTipo.ADMIN);

        setupSaveTest(testUser);
    }

    @Test(expected = ClienteHasMoreThanOnePerfilException.class)
    public void shouldBlockSaveActionBecauseUserHasMoreThanOneProfileWhenCliente(){
        Usuario testUser = new Usuario();

        testUser.setAtivo(true);
        testUser.addPerfil(PerfilTipo.CLIENTE);
        testUser.addPerfil(PerfilTipo.ADMIN);

        setupSaveTest(testUser);
    }

    @Test
    public void shouldDeleteUser(){
        final long mockedID = 1L;
        final String mockedEmail = "teste@gmail.com";
        final String anotherMockedEmail = "teste2@gmail.com";

        //Mocka e cria objetos de dependencia
        User springUser = Mockito.mock(User.class);
        Mockito.when(springUser.getUsername()).thenReturn(anotherMockedEmail);

        Optional<Usuario> optionalMockedUser = Optional.of(createMockedUserById(mockedID));
        Usuario mockedUser = optionalMockedUser.get();
        Mockito.when(mockedUser.getEmail()).thenReturn(mockedEmail);

        //Mocka repositorios
        Mockito.when(userRepo.findById(ArgumentMatchers.eq(mockedID))).thenReturn(optionalMockedUser);

        Optional<Pessoa> optionalMockedPessoa = Optional.of(Mockito.mock(Pessoa.class));
        Pessoa mockedPessoa = optionalMockedPessoa.get();
        Mockito.when(mockedPessoa.getId()).thenReturn(mockedID);
        Mockito.when(pessoaRepository.findByUserId(ArgumentMatchers.any(Long.class))).thenReturn(optionalMockedPessoa);

        List<CargoHistorico> historicoMock = new ArrayList<>();
        Mockito.when(cargoHistoricoRepository.findAllByUserId(ArgumentMatchers.any(Long.class))).thenReturn(historicoMock);

        //Testa
        userService.deleteUser(mockedID, springUser);
        Mockito.verify(mockedUser, Mockito.times(1)).getEmail();
        Mockito.verify(springUser, Mockito.times(1)).getUsername();
        Mockito.verify(cargoHistoricoRepository, Mockito.times(1)).findAllByUserId(ArgumentMatchers.any(Long.class));
        Mockito.verify(userRepo, Mockito.times(1)).delete(ArgumentMatchers.any(Usuario.class));
    }

    @Test(expected = UsuarioNotFoundException.class)
    public void shouldThrowUsuarioNotFoundExceptionWhenTryingDeleteUser(){
        final long mockedID = 1L;
        final String mockedEmail = "teste@gmail.com";
        final String anotherMockedEmail = "teste2@gmail.com";

        //Mocka e cria objetos de dependencia
        User springUser = Mockito.mock(User.class);
        Optional<Usuario> optionalMockedUser = Optional.of(createMockedUserById(mockedID));
        Usuario mockedUser = optionalMockedUser.get();

        Optional<Pessoa> optionalMockedPessoa = Optional.of(Mockito.mock(Pessoa.class));
        Pessoa mockedPessoa = optionalMockedPessoa.get();

        //Testa
        userService.deleteUser(mockedID, springUser);
        Mockito.verify(userRepo, Mockito.times(0)).delete(ArgumentMatchers.any(Usuario.class));
    }

    @Test(expected = SelfExclusionException.class)
    public void shouldThrowSelfExclusionException(){
        final long mockedID = 1L;
        final String mockedEmail = "teste@gmail.com";

        //Mocka e cria objetos de dependencia
        User springUser = Mockito.mock(User.class);
        Mockito.when(springUser.getUsername()).thenReturn(mockedEmail);

        Optional<Usuario> optionalMockedUser = Optional.of(createMockedUserById(mockedID));
        Usuario mockedUser = optionalMockedUser.get();
        Mockito.when(mockedUser.getEmail()).thenReturn(mockedEmail);

        //Mocka repositorios
        Mockito.when(userRepo.findById(ArgumentMatchers.eq(mockedID))).thenReturn(optionalMockedUser);

        Optional<Pessoa> optionalMockedPessoa = Optional.of(Mockito.mock(Pessoa.class));
        Pessoa mockedPessoa = optionalMockedPessoa.get();
        Mockito.when(mockedPessoa.getId()).thenReturn(mockedID);
        Mockito.when(pessoaRepository.findByUserId(ArgumentMatchers.any(Long.class))).thenReturn(optionalMockedPessoa);

        //Testa
        userService.deleteUser(mockedID, springUser);
    }

    @Test(expected = HasCargosException.class)
    public void shouldThrowHasCargoExceptioWhenTryingToDeleteUser() {
        final long mockedID = 1L;
        final String mockedEmail = "teste@gmail.com";
        final String anotherMockedEmail = "teste2@gmail.com";

        //Mocka e cria objetos de dependencia
        User springUser = Mockito.mock(User.class);
        Mockito.when(springUser.getUsername()).thenReturn(anotherMockedEmail);

        Optional<Usuario> optionalMockedUser = Optional.of(createMockedUserById(mockedID));
        Usuario mockedUser = optionalMockedUser.get();
        Mockito.when(mockedUser.getEmail()).thenReturn(mockedEmail);

        //Mocka repositorios
        Mockito.when(userRepo.findById(ArgumentMatchers.eq(mockedID))).thenReturn(optionalMockedUser);

        Optional<Pessoa> optionalMockedPessoa = Optional.of(Mockito.mock(Pessoa.class));
        Pessoa mockedPessoa = optionalMockedPessoa.get();
        Mockito.when(mockedPessoa.getId()).thenReturn(mockedID);
        Mockito.when(pessoaRepository.findByUserId(ArgumentMatchers.any(Long.class))).thenReturn(optionalMockedPessoa);

        List<CargoHistorico> historicoMock = new ArrayList<>();
        historicoMock.add(new CargoHistorico());
        Mockito.when(cargoHistoricoRepository.findAllByUserId(ArgumentMatchers.any(Long.class))).thenReturn(historicoMock);

        //Testa
        userService.deleteUser(mockedID, springUser);
        Mockito.verify(cargoHistoricoRepository, Mockito.times(1)).findAllByUserId(ArgumentMatchers.any(Long.class));
    }

    //############################# HELP METHODS

    private void setupSaveTest(Usuario testUser){
        testUser.setSenha("123456");
        testUser.setEmail("teste@gmail.com");
        UserCargo userCargo = new UserCargo(CargoTipo.AUXILIAR_ADMINISTRATIVO.buildCargo());
        userCargo.setDtAdm(LocalDate.now());
        testUser.setUserCargo(userCargo);


        List<CargoHistorico> historicoMock = new ArrayList<>();

        Mockito.when(userRepo.save(ArgumentMatchers.any(Usuario.class))).thenReturn(testUser);

        userService.saveUser(testUser);

        Mockito.verify(userRepo, Mockito.times(1)).save(ArgumentMatchers.any(Usuario.class));
    }

    //Mocka um usuario
    private Usuario createMockedUser(String mockedUsername) {
        Usuario user = Mockito.mock(Usuario.class);
        Mockito.when(user.getId())
                .thenReturn(0L);
        Mockito.when(user.getEmail())
                .thenReturn(mockedUsername);
        Mockito.when(user.getSenha())
                .thenReturn("");
        return user;
    }
    //Mocka um usuario por id
    private Usuario createMockedUserById(long id) {
        Usuario user = Mockito.mock(Usuario.class);
        Mockito.when(user.getId())
                .thenReturn(id);
        return user;
    }

}
