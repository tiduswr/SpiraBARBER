package spyrabarber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spyrabarber.domain.Servico;
import spyrabarber.domain.dto.ServicoDTO;
import spyrabarber.repository.ServicosRepository;
import spyrabarber.service.util.FileUtil;
import spyrabarber.web.exception.ImageManageException;
import spyrabarber.web.exception.NotUniqueNameException;
import spyrabarber.web.exception.ServicoHasDependencyException;
import spyrabarber.web.exception.ServicoNotFoundException;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class ServicosService {

    @Autowired
    private ServicosRepository servicosRepository;

    private final String IMAGE_FOLDER = "servicos";

    @Autowired
    private FileUtil fileUtil;

    private String encryptImagesMessageDigestType = "MD5";

    public String getEncryptImagesMessageDigestType() {
        return encryptImagesMessageDigestType;
    }

    public void setEncryptImagesMessageDigestType(String encryptImagesMessageDigestType) {
        this.encryptImagesMessageDigestType = encryptImagesMessageDigestType;
    }

    @Transactional
    public void saveServico(ServicoDTO dto) throws ImageManageException, NotUniqueNameException {

        boolean saveImage = true;
        if(dto.getImage() != null && !dto.getImage().isEmpty()) {
            testImageFile(dto.getImage());
        }else if(dto.getId() == null) {
            throw new ImageManageException("É necessário inserir uma imagem!", "image");
        }else{
            saveImage = false;
        }

        if(!testIfNameIsUnique(dto.getNome(), dto.getId()))
            throw new NotUniqueNameException("O nome do serviço ja existe!", "nome");

        Servico s;
        if(dto.getId() != null){
            s = servicosRepository.findById(dto.getId()).orElse(new Servico());
        }else{
            s = new Servico();
        }

        s.setDescricao(dto.getDescricao());
        s.setNome(dto.getNome());
        s.setPreco(dto.getPreco());
        s.setTempo(dto.getTempo());

        if(saveImage){
            s.setImageName("temp_name");
            s = servicosRepository.save(s);
            String imagename = "servico-" + s.getId();
            try {
                MessageDigest m = MessageDigest.getInstance(encryptImagesMessageDigestType);
                m.update(imagename.getBytes(),0,imagename.length());
                imagename = new BigInteger(1,m.digest()).toString(16);
            } catch (NoSuchAlgorithmException e) {
                throw new ImageManageException("Erro ao salvar a imagem no servidor!", "image");
            }
            fileUtil.saveFile(imagename, dto.getImage(), IMAGE_FOLDER);
            s.setImageName(imagename);
        }

        servicosRepository.save(s);
    }

    @Transactional(readOnly = true)
    public boolean testIfNameIsUnique(String nome, Long id){
        if(id == null){
            return servicosRepository.findByName(nome).isEmpty();
        }else{
            Optional<Servico> s = servicosRepository.findByName(nome);
            if(s.isEmpty()) return true;
            return s.get().getId().equals(id);
        }
    }

    private void testImageFile(MultipartFile image) throws ImageManageException{
        String fullContentType = image.getContentType();
        String[] contentTypeSplited;
        if(fullContentType != null) {
            contentTypeSplited = fullContentType.split("/");
            if(!contentTypeSplited[0].equalsIgnoreCase("image")){
                throw new ImageManageException("Esse arquivo não é uma imagem.", "image");
            }
        }else{
            throw new ImageManageException("Esse arquivo não é uma imagem.", "image");
        }
    }

    @Transactional(readOnly = true)
    public List<Servico> getAllServicos() {
        return servicosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicosRepository.findById(id).orElseThrow(() -> new ServicoNotFoundException("Serviço não encontrado."));
    }

    @Transactional
    public void deleteServico(Servico s) {
        try{
            fileUtil.deleteFile(s.getImageName(), IMAGE_FOLDER);
        }catch (ImageManageException ex){
            throw new ImageManageException("Não foi possivel excluir a imagem", "image");
        }

        if(!s.getUsers().isEmpty()) throw new ServicoHasDependencyException("Esse serviço possui dependencias");

        servicosRepository.delete(s);
    }
}
