/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HP
 */
public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException{
        Path uploadPath = Paths.get(uploadDir);
        
    if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);

    }  
    
    try (InputStream inputStream = multipartFile.getInputStream()){
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ioe){
        throw new IOException("Could not save image file : " + fileName, ioe);    
    }
    }
    
}
