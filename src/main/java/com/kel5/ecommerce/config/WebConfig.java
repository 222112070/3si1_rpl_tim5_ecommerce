package com.kel5.ecommerce.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/css/**", "/images/**", "/js/**", "/static/**")
//                .addResourceLocations("classpath:/static/css/", "classpath:/static/images/", "classpath:/static/js/", "classpath:/static/");
//    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        exposeDirectory("blog-photos", registry);
        String baseDir = System.getProperty("user.dir");
        registry.addResourceHandler("/productsImages/**")
                .addResourceLocations("file:" + baseDir + "/productsImages/")
                .setCachePeriod(0);
    }
    
    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry){
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
    }
    
}
