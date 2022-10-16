package spyrabarber.service.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import spyrabarber.web.exception.ImageManageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUtil {
    private final String IMAGE_FOLDER = "uploaded-images/";

    public void saveFile(String fileName, MultipartFile file, String folder) throws ImageManageException{
        Path uploadDirectory = Paths.get(IMAGE_FOLDER + folder);
        try(InputStream inputStream = file.getInputStream()){
            if(Files.exists(uploadDirectory.resolve(fileName))) Files.delete(uploadDirectory.resolve(fileName));
            BufferedImage b = ImageIO.read(inputStream);
            ImageIO.write(b, "png", uploadDirectory.resolve(fileName + ".png").toFile());
        } catch (IOException e) {
            throw new ImageManageException("Erro ao salvar a imagem!", "image");
        }
    }

    public void deleteFile(String fileName, String folder) throws ImageManageException{
        Path uploadDirectory = Paths.get(IMAGE_FOLDER + folder);
        Path filePath = uploadDirectory.resolve(fileName + ".png");
        try{
            Files.delete(filePath);
        } catch (IOException e) {
            throw new ImageManageException("Erro ao deletar a imagem!", "image");
        }
    }

    public Path returnFilePath(String fileName, String folder){
        Path uploadDirectory = Paths.get(IMAGE_FOLDER + folder);
        return uploadDirectory.resolve(fileName + ".png");
    }

    public String getIMAGE_FOLDER(){
        return IMAGE_FOLDER;
    }

}
