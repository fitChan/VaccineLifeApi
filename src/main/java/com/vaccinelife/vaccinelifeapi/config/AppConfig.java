package com.vaccinelife.vaccinelifeapi.config;

import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.UserRole;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            UserService userService;

            //Test용 User.
            @Override
            public void run(ApplicationArguments args) throws Exception {
                SignupRequestDto cksdntjd = SignupRequestDto.builder()
                        .username("cksdntjd")
                        .password("cksdn123")
                        .nickname("chanoo")
                        .isVaccine(true)
                        .type("화이자")
                        .degree(2)
                        .gender("남")
                        .age("28")
                        .disease("없음")
                        .afterEffect("인후통")
                        .build();
                userService.registerUser(cksdntjd);
            }
        };
    }
}
