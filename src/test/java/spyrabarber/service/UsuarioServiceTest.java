package spyrabarber.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import config.ApplicationConfigTest;
import spyrabarber.domain.Usuario;
import spyrabarber.repository.UsuarioRepository;

import java.util.Optional;

@DisplayName("Testando Usuario service")
public class UsuarioServiceTest extends ApplicationConfigTest {

    @MockBean
    private UsuarioRepository userRepo;

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

}
