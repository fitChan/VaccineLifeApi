package com.vaccinelife.vaccinelifeapi.config;

import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            UserService userService;
            @Autowired
            VacBoardRepository vacBoardRepository;
@Autowired
            UserRepository userRepository;
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
                User user = userRepository.findById(1L).orElseThrow(
                        ()-> new IllegalArgumentException("없는 유저입니다.")
                );
                for (int i = 0; i < 50; i++) {
                    VacBoard vacBoard = VacBoard.builder()
                            .user(user)
                            .title("the title"+i+"번째 for Junit Test")
                            .contents("the content"+i+"번째 for Junit Test")
                            .build();
                    vacBoardRepository.save(vacBoard);
                }
            }
        };
    }
}
