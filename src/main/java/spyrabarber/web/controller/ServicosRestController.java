package spyrabarber.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spyrabarber.service.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/servicos")
public class ServicosRestController {

    @Autowired
    private FileUtil fileUtil;

    private final String IMAGE_FOLDER = "servicos";

    @GetMapping("/image/{image}")
    public byte[] getImage(@PathVariable("image") String image) throws IOException {
        if(image != null && image.trim().length() > 0){
            return Files.readAllBytes(fileUtil.returnFilePath(image, IMAGE_FOLDER));
        }else{
            return null;
        }
    }

}
