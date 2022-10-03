package spyrabarber.service;

import config.ApplicationConfigTest;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import spyrabarber.domain.Pessoa;
import spyrabarber.domain.Usuario;
import spyrabarber.repository.PessoaRepository;
import spyrabarber.repository.UsuarioRepository;
import spyrabarber.web.exception.UsuarioNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Testando pessoa service")
public class PessoaServiceTest extends ApplicationConfigTest {

    @MockBean
    private PessoaRepository pessoaRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaService pessoaService;

    @Test
    public void shouldFindByUserId(){
        final long mockedId = 1L;
        Pessoa mockedPessoa = Mockito.mock(Pessoa.class);
        Mockito.when(mockedPessoa.getId()).thenReturn(mockedId);
        Optional<Pessoa> optionalMockedPessoa = Optional.of(mockedPessoa);

        Mockito.when(pessoaRepository.findByUserId(ArgumentMatchers.eq(mockedId))).thenReturn(optionalMockedPessoa);

        Pessoa returnedValue = pessoaService.buscarPorUsuarioId(mockedId);

        Mockito.verify(pessoaRepository, Mockito.times(1)).findByUserId(ArgumentMatchers.any());
        assertThat(returnedValue.getId()).isEqualTo(mockedId);
    }

    @Test
    public void shouldReturnEmptyUserWhenCannotFindByUserId(){
        final long mockedId = 1L;
        Pessoa returnedValue = pessoaService.buscarPorUsuarioId(mockedId);
        assertNull(returnedValue.getName());
    }

    @Test
    public void shouldUpdatePessoa(){
        final long mockedId = 1L;
        Optional<Usuario> optionalMockedUser = Optional.of(Mockito.mock(Usuario.class));
        Usuario mockedUser = optionalMockedUser.get();
        Mockito.when(mockedUser.getId()).thenReturn(mockedId);

        Optional<Pessoa> optionalMockedPessoa = Optional.of(Mockito.mock(Pessoa.class));
        Pessoa mockedPessoa = optionalMockedPessoa.get();
        Mockito.when(mockedPessoa.getUser()).thenReturn(mockedUser);
        Mockito.doCallRealMethod().when(mockedPessoa).setUser(ArgumentMatchers.any(Usuario.class));

        Mockito.when(usuarioRepository.findById(ArgumentMatchers.eq(mockedId))).thenReturn(optionalMockedUser);

        pessoaService.atualizarUsuarioComDadosPessoais(mockedPessoa);
        Mockito.verify(pessoaRepository, Mockito.times(1)).save(ArgumentMatchers.any(Pessoa.class));
    }

    @Test(expected = UsuarioNotFoundException.class)
    public void shouldThrowUserNotFoundWhenHasNoUserInPessoaObject(){
        Optional<Pessoa> optionalMockedPessoa = Optional.of(Mockito.mock(Pessoa.class));
        Pessoa mockedPessoa = optionalMockedPessoa.get();

        pessoaService.atualizarUsuarioComDadosPessoais(mockedPessoa);
        Mockito.verify(mockedPessoa, Mockito.times(1)).getUser();
    }

    @Test(expected = UsuarioNotFoundException.class)
    public void shouldThrowUserNotFoundWhenRepositoryCantFind(){
        final long mockedId = 1L;
        Optional<Usuario> optionalMockedUser = Optional.of(Mockito.mock(Usuario.class));
        Usuario mockedUser = optionalMockedUser.get();
        Mockito.when(mockedUser.getId()).thenReturn(mockedId);

        Optional<Pessoa> optionalMockedPessoa = Optional.of(Mockito.mock(Pessoa.class));
        Pessoa mockedPessoa = optionalMockedPessoa.get();
        Mockito.when(mockedPessoa.getUser()).thenReturn(mockedUser);

        pessoaService.atualizarUsuarioComDadosPessoais(mockedPessoa);
        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
    }

}
