package com.kel5.ecommerce.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Autowired
    private LoadingInterceptor loadingInterceptor;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        int cachePeriod = 60 * 60 * 24 * 30;

        exposeDirectory("blog-photos", registry);
        String baseDir = System.getProperty("user.dir");
        registry.addResourceHandler("/productImages/**")
                .addResourceLocations("file:" + baseDir + "/productImages/")
                .setCachePeriod(0);
        
        registry.addResourceHandler("/css/**", "/images/**", "/js/**", "/static/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/images/", "classpath:/static/js/", "classpath:/static/")
                .setCachePeriod(cachePeriod);

    }
    
    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry){
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
    }
    
    @Bean(name = "customLoadingInterceptor")
    public LoadingInterceptor loadingInterceptor() {
        return new LoadingInterceptor();
    }
}
