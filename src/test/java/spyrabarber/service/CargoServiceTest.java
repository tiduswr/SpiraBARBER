package spyrabarber.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import spyrabarber.domain.*;
import spyrabarber.domain.dto.CargoDTO;
import spyrabarber.repository.CargoHistoricoRepository;
import spyrabarber.repository.CargoRepository;
import spyrabarber.repository.UserCargoRepository;
import spyrabarber.repository.UsuarioRepository;
import spyrabarber.web.exception.UsuarioNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DisplayName("Testa CargoService")
public class CargoServiceTest{

    @MockBean
    private UsuarioRepository userRepo;

    @MockBean
    private CargoRepository cargoRepository;

    @MockBean
    private UserCargoRepository userCargoRepository;

    @MockBean
    private CargoHistoricoRepository cargoHistoricoRepository;

    @Autowired
    private CargoService cargoService;

    @Test
    public void shouldFindAllCargos(){
        List<Cargo> mockedList = new ArrayList<>();

        Mockito.when(cargoRepository.findAll()).thenReturn(mockedList);

        assertNotNull(cargoService.buscarTodosOsCargos());
        Mockito.verify(cargoRepository, Mockito.times(1)).findAll();
    }

    @Test(expected = UsuarioNotFoundException.class)
    public void shouldThrowUsuarioNotFoundExceptionWhenBuildCargoDTOWithoutUser(){
        final long mockedUserId = 1L;
        cargoService.montarCargoDTOByUser(mockedUserId);
    }

    @Test
    public void shouldBuildCargoDTOByUserID(){
        final long mockedUserId = 1L;

        Optional<Usuario> optMockedUser = Optional.of(Mockito.mock(Usuario.class));
        Usuario usuario = optMockedUser.get();
        Mockito.when(usuario.getUserCargo()).thenReturn(new UserCargo(CargoTipo.AUXILIAR_ADMINISTRATIVO.buildCargo()));
        Mockito.when(userRepo.findById(ArgumentMatchers.eq(mockedUserId))).thenReturn(optMockedUser);

        List<CargoHistorico> mockedList = new ArrayList<>();
        Mockito.when(cargoHistoricoRepository.findAllByUserId(ArgumentMatchers.eq(mockedUserId))).thenReturn(mockedList);

        assertNotNull(cargoService.montarCargoDTOByUser(mockedUserId));
        Mockito.verify(userRepo, Mockito.times(1)).findById(ArgumentMatchers.any());
        Mockito.verify(cargoHistoricoRepository, Mockito.times(1)).findAllByUserId(ArgumentMatchers.any());
    }

    @Test
    public void shouldNotSaveCargoWhenHasInvalidData(){
        final long mockedUserId = 1L;
        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(usuario.getId()).thenReturn(mockedUserId);

        List<CargoHistorico> mockedHistorico = new ArrayList<>();
        CargoHistorico ch = new CargoHistorico();
        ch.setDtAdm(LocalDate.of(2021,12,1));
        ch.setDtDemissao(LocalDate.of(2022,1,1));
        mockedHistorico.add(ch);
        Mockito.when(cargoHistoricoRepository.findAllByUserId(mockedUserId)).thenReturn(mockedHistorico);

        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setDtAdmissao(LocalDate.of(2022,1,1));
        cargoDTO.setUser(usuario);

        BindingResult br = Mockito.mock(BeanPropertyBindingResult.class);

        assertFalse(cargoService.atualizarCargo(cargoDTO, br));

        cargoDTO.setDtAdmissao(LocalDate.of(2022,1,2));
        cargoDTO.setDtDemissao(LocalDate.of(2022,1,1));

        assertFalse(cargoService.atualizarCargo(cargoDTO, br));

        cargoDTO.setDtAdmissao(LocalDate.now().plusDays(1));
        cargoDTO.setDtDemissao(null);

        assertFalse(cargoService.atualizarCargo(cargoDTO, br));

        cargoDTO.setDtAdmissao(LocalDate.of(2022,1,1));
        cargoDTO.setDtDemissao(LocalDate.now().plusDays(1));

        assertFalse(cargoService.atualizarCargo(cargoDTO, br));
    }

    @Test(expected = UsuarioNotFoundException.class)
    public void shouldThrowUsuarioNotFoundExceptionWhenTryingToSaveCargo(){
        final long mockedUserId = 1L;
        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(usuario.getId()).thenReturn(mockedUserId);

        List<CargoHistorico> mockedHistorico = new ArrayList<>();
        Mockito.when(cargoHistoricoRepository.findAllByUserId(mockedUserId)).thenReturn(mockedHistorico);

        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setDtAdmissao(LocalDate.of(2022,1,1));
        cargoDTO.setUser(usuario);

        BindingResult br = Mockito.mock(BeanPropertyBindingResult.class);
        cargoService.atualizarCargo(cargoDTO, br);
        Mockito.verify(userRepo, Mockito.times(1)).findById(ArgumentMatchers.eq(mockedUserId));
    }

    @Test
    public void shouldSaveCargoWhenDemissaoDateIsNull(){
        final long mockedUserId = 1L;
        Optional<Usuario> optionalUsuario = Optional.of(Mockito.mock(Usuario.class));
        Usuario usuario = optionalUsuario.get();
        Mockito.when(usuario.getId()).thenReturn(mockedUserId);
        Mockito.doCallRealMethod().when(usuario).setUserCargo(ArgumentMatchers.any(UserCargo.class));
        Mockito.when(userRepo.findById(ArgumentMatchers.eq(mockedUserId))).thenReturn(optionalUsuario);

        List<CargoHistorico> mockedHistorico = new ArrayList<>();
        Mockito.when(cargoHistoricoRepository.findAllByUserId(mockedUserId)).thenReturn(mockedHistorico);

        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setCargo(CargoTipo.AUXILIAR_ADMINISTRATIVO.buildCargo());
        cargoDTO.setDtAdmissao(LocalDate.of(2022,1,1));
        cargoDTO.setUser(usuario);

        Mockito.when(userCargoRepository.save(ArgumentMatchers.any(UserCargo.class))).thenReturn(new UserCargo());
        Mockito.when(userRepo.save(ArgumentMatchers.any(Usuario.class))).thenReturn(new Usuario());

        BindingResult br = Mockito.mock(BeanPropertyBindingResult.class);
        cargoService.atualizarCargo(cargoDTO, br);

        Mockito.verify(userRepo, Mockito.times(1)).save(ArgumentMatchers.any(Usuario.class));
        Mockito.verify(userCargoRepository, Mockito.times(1)).save(ArgumentMatchers.any(UserCargo.class));
    }

    @Test
    public void shouldSaveCargoWhenDemissaoDateIsNotNull(){
        final long mockedUserId = 1L;
        Optional<Usuario> optionalUsuario = Optional.of(Mockito.mock(Usuario.class));
        Usuario usuario = optionalUsuario.get();
        Mockito.when(usuario.getId()).thenReturn(mockedUserId);
        Mockito.doCallRealMethod().when(usuario).setUserCargo(ArgumentMatchers.any(UserCargo.class));
        Mockito.when(userRepo.findById(ArgumentMatchers.eq(mockedUserId))).thenReturn(optionalUsuario);

        List<CargoHistorico> mockedHistorico = new ArrayList<>();
        Mockito.when(cargoHistoricoRepository.findAllByUserId(mockedUserId)).thenReturn(mockedHistorico);

        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setCargo(CargoTipo.AUXILIAR_ADMINISTRATIVO.buildCargo());
        cargoDTO.setDtAdmissao(LocalDate.of(2022,1,1));
        cargoDTO.setDtDemissao(LocalDate.of(2022,2,1));
        cargoDTO.setUser(usuario);

        Mockito.when(userCargoRepository.save(ArgumentMatchers.any(UserCargo.class))).thenReturn(new UserCargo());
        Mockito.when(userRepo.save(ArgumentMatchers.any(Usuario.class))).thenReturn(new Usuario());
        Mockito.when(cargoHistoricoRepository.save(ArgumentMatchers.any(CargoHistorico.class))).thenReturn(new CargoHistorico());

        BindingResult br = Mockito.mock(BeanPropertyBindingResult.class);
        cargoService.atualizarCargo(cargoDTO, br);

        Mockito.verify(cargoHistoricoRepository, Mockito.times(1)).save(ArgumentMatchers.any(CargoHistorico.class));
        Mockito.verify(userRepo, Mockito.times(1)).save(ArgumentMatchers.any(Usuario.class));
        Mockito.verify(userCargoRepository, Mockito.times(1)).delete(ArgumentMatchers.any(UserCargo.class));
    }

}
