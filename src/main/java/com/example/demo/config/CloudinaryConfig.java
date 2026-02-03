package com.example.demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", System.getenv("cloudinary.cloud_name"),
                "api_key", System.getenv("cloudinary.api_key"),
                "api_secret", System.getenv("cloudinary.api_secret"),
                "secure", true
        ));
    }
}
