package com.ll.whev.domain.rekognition;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

@Configuration
@RequiredArgsConstructor
public class RekognitionClientConfig {

    @Bean
    public RekognitionClient rekognitionClient() {
        return RekognitionClient.builder()
                .region(Region.US_EAST_1) // 원하는 리전을 설정하세요
                .build();
    }

}
