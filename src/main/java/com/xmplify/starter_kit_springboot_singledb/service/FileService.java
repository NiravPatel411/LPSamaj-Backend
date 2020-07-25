package com.xmplify.starter_kit_springboot_singledb.service;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // type : Image,PDF,VIDEO
    // event : News,Activity,Profile
    public String uploadFile(MultipartFile file, ServletContext context, String type, String event) {

        try {
            byte[] bytes = file.getBytes();
            String fullPath = context.getRealPath(GlobalConstants.UPLOAD_DIR + type + GlobalConstants.BACK_SLASH + event + GlobalConstants.BACK_SLASH);
            Path path = Paths.get(fullPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String storePath = UUID.randomUUID().toString() + GlobalConstants.DOT + file.getOriginalFilename().split("\\.(?=[^\\.]+$)")[1];
            Path filePath = Paths.get(path + GlobalConstants.BACK_SLASH + storePath);
            Files.write(filePath, bytes);
            return storePath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getDeleveryPath(String storePath,String type, String event){
        String directoryPath = getDirectoryPath(type,event);
        String deleveryPath = ServletUriComponentsBuilder.fromCurrentContextPath().path(directoryPath + storePath).toUriString();
        return deleveryPath;
    }

    private String getDirectoryPath(String type, String event) {
        return GlobalConstants.BACK_SLASH + type + GlobalConstants.BACK_SLASH + event + GlobalConstants.BACK_SLASH;
    }

    public boolean deleteFile(String storePath, String type, String event, ServletContext context){
        String fullPath = context.getRealPath(GlobalConstants.UPLOAD_DIR + type + GlobalConstants.BACK_SLASH + event + GlobalConstants.BACK_SLASH);
        Path path = Paths.get(fullPath);
        Path filePath = Paths.get(path + File.separator + storePath);
        try {
            Files.delete(filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
