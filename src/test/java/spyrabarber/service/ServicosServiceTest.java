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
import org.springframework.web.multipart.MultipartFile;
import spyrabarber.domain.Servico;
import spyrabarber.domain.Usuario;
import spyrabarber.domain.dto.ServicoDTO;
import spyrabarber.repository.ServicosRepository;
import spyrabarber.service.util.FileUtil;
import spyrabarber.web.exception.ImageManageException;
import spyrabarber.web.exception.ServicoHasDependencyException;
import spyrabarber.web.exception.ServicoNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DisplayName("Testando servicos service")
public class ServicosServiceTest{

    @MockBean
    private ServicosRepository servicosRepository;

    @MockBean
    private FileUtil fileUtil;

    @Autowired
    private ServicosService servicosService;

    @Test
    public void shouldTestIfNameIsUnique(){
        final String name = "teste";
        final long id = 1L;
        final Servico mock = new Servico(id);
        mock.setNome(name);
        Optional<Servico> optMock = Optional.of(mock);

        Mockito.when(servicosRepository.findByName(ArgumentMatchers.eq(name))).thenReturn(optMock);

        //Ignora o objeto que tenha o mesmo nome e mesmo id
        assertTrue(servicosService.testIfNameIsUnique(name, id));
        //Se existir o mesmo nome na base de dados e for um objeto de id diferente deve retornar false
        assertFalse(servicosService.testIfNameIsUnique(name, 2L));

        Mockito.verify(servicosRepository, Mockito.times(2)).findByName(ArgumentMatchers.anyString());
    }

    @Test
    public void shouldReturnAllServicos(){
        List<Servico> mockedList = new ArrayList<>();
        mockedList.add(new Servico());

        Mockito.when(servicosRepository.findAll()).thenReturn(mockedList);

        assertNotNull(servicosService.getAllServicos());
        Mockito.verify(servicosRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldFindServicoByID(){
        final long mockID = 1L;
        final Servico mock = new Servico(mockID);
        Optional<Servico> optMock = Optional.of(mock);

        Mockito.when(servicosRepository.findById(ArgumentMatchers.eq(mockID))).thenReturn(optMock);

        assertNotNull(servicosService.buscarPorId(mockID));
        assertThrows(ServicoNotFoundException.class, () -> servicosService.buscarPorId(2L));

        Mockito.verify(servicosRepository, Mockito.times(2)).findById(ArgumentMatchers.any(Long.class));
    }

    @Test
    public void shouldTestDeleteServico(){
        final long mockID = 1L;
        final Servico mock = new Servico(mockID);
        mock.setUsers(new HashSet<>());
        mock.setImageName("dump");

        Mockito.doNothing().when(fileUtil).deleteFile(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.doNothing().when(servicosRepository).delete(ArgumentMatchers.any(Servico.class));
        servicosService.deleteServico(mock);

        Mockito.reset(fileUtil);
        Mockito.doThrow(ImageManageException.class).when(fileUtil)
                .deleteFile(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        assertThrows(ImageManageException.class, () -> servicosService.deleteServico(mock));

        Mockito.reset(fileUtil);
        Mockito.doNothing().when(fileUtil).deleteFile(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        mock.getUsers().add(new Usuario());
        assertThrows(ServicoHasDependencyException.class, () -> servicosService.deleteServico(mock));

        Mockito.verify(servicosRepository, Mockito.times(1)).delete(ArgumentMatchers.any(Servico.class));
    }

    @Test
    public void shouldTestSaveServico(){
        final long mockID = 1L;
        final String mockName = "TESTE";

        ServicoDTO dto = new ServicoDTO(mockID);
        dto.setNome(mockName);
        dto.setDescricao("alguma descrição");
        dto.setPreco(new BigDecimal("19.99"));
        dto.setTempo(15);

        //Testa quando o tipo da imagem é nulo
        dto.setImage(configMockedMultipartFile(null, false));
        assertThrows(ImageManageException.class, () -> servicosService.saveServico(dto));

        //Testa quando o tipo da imagem não é imagem
        dto.setImage(configMockedMultipartFile("invalid", false));
        assertThrows(ImageManageException.class, () -> servicosService.saveServico(dto));

        //Testa quando o id é nulo e a imagem também
        dto.setId(null);
        dto.setImage(configMockedMultipartFile(null, false));
        assertThrows(ImageManageException.class, () -> servicosService.saveServico(dto));

        //Verifica se o nome ja existe(Teste separado, escolhendo o melhor caso abaixo)
        Mockito.when(servicosRepository.findByName(ArgumentMatchers.eq(mockName)))
                .thenReturn(Optional.empty());

        //Configura função save do repositório e adiciona uma imagem válida
        dto.setImage(configMockedMultipartFile("image/png", false));
        Servico mockServico = new Servico(dto);
        mockServico.setId(mockID);
        Mockito.when(servicosRepository.save(ArgumentMatchers.any(Servico.class))).thenReturn(mockServico);

        //Testa quando não é possivel encriptar o nome da imagem
        servicosService.setEncryptImagesMessageDigestType("error");
        assertThrows(ImageManageException.class, () -> servicosService.saveServico(dto));

        //Mocka a ação de salvar imagem
        Mockito.doNothing().when(fileUtil)
                .saveFile(ArgumentMatchers.anyString(),
                        ArgumentMatchers.any(MultipartFile.class),
                        ArgumentMatchers.anyString());

        //Precisa funcionar com todos os inputs válidos
        servicosService.setEncryptImagesMessageDigestType("MD5");
        servicosService.saveServico(dto);

        //Verifica se os métodos foram realmente chamados
        Mockito.verify(servicosRepository, Mockito.times(3))
                .save(ArgumentMatchers.any(Servico.class));
        Mockito.verify(servicosRepository, Mockito.times(2))
                .findByName(ArgumentMatchers.eq(mockName));
    }

    private MultipartFile configMockedMultipartFile(String contentType, boolean empty) {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.isEmpty()).thenReturn(empty);
        Mockito.when(file.getContentType()).thenReturn(contentType);
        return file;
    }

}
