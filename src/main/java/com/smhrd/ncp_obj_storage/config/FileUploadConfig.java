package com.smhrd.ncp_obj_storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    // application.properties 에 있는 file.upload-dir 참조
    @Value("${file.upload-dir}")
    private String uploadDir; // C:/upload 라는 폴더

    //저장 될 경로를 수정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")  // 브라우저에서 접근할 경로를 설정
                //localhost:포트/uploads/파일명
                .addResourceLocations("file:///"+uploadDir); // 실제 서버에 저장할 경로

    }

}
