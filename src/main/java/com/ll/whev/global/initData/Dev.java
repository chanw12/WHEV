package com.ll.whev.global.initData;

import com.ll.whev.domain.image.dto.ImageSaveDto;
import com.ll.whev.domain.image.service.ImageService;
import com.ll.whev.global.app.AppConfig;
import com.ll.whev.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class Dev {
    @Bean
    @Order(4)
    ApplicationRunner initDev() {
        return args -> {
            String backUrl = AppConfig.getSiteBackUrl();
            String cmd = "npx openapi-typescript " + backUrl + "/v3/api-docs/apiV1 -o ./frontapp/src/lib/types/api/v1/schema.d.ts";
            Ut.cmd.runAsync(cmd);
        };
    }



// ...

//    @Bean
//    @Order(5)
//    ApplicationRunner initImages(JdbcTemplate jdbcTemplate) {
//        return args -> {
//            String sql = "INSERT INTO image (create_date, member_id, content, tags, path) VALUES (?, ?, ?, ?, ?)";
//
//            for (int i = 0; i < 120; i+=3) {
//                String content = "Sample Content " + i;
//                String tags = "tag1,tag2";
//                String path = "/zb5a852024-04-11T13:43:55.411999_39c03587-ea66-4b7f-abf5-362d4cd4538e 복사본.jpeg";
//                LocalDateTime create_date = LocalDateTime.now();
//                Long member_id = 1L;
//
//                jdbcTemplate.update(sql, create_date, member_id, content, tags, path);
//
//                content = "Sample Content " + i+1;
//                tags = "tag1,tag2";
//                path = "/0dv9ty2024-04-11T13:48:02.354273강찬우-여권.jpeg";
//                create_date = LocalDateTime.now();
//                member_id = 1L;
//                jdbcTemplate.update(sql, create_date, member_id, content, tags, path);
//
//
//                content = "Sample Content " + i+2;
//                tags = "tag1,tag2";
//                path = "/8wvzrg2024-04-11T16:21:30.5481242002148917.png";
//                create_date = LocalDateTime.now();
//                member_id = 1L;
//                jdbcTemplate.update(sql, create_date, member_id, content, tags, path);
//
//
//
//            }
//        };
//    }
}
