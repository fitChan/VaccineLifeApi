package com.vaccinelife.vaccinelifeapi.config;

import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.model.enums.AfterEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class AppConfig {




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
                        .type(Type.AZ)
                        .degree(2)
                        .gender("남")
                        .age("28")
                        .disease("없음")
                        .afterEffect(Collections.singleton(AfterEffect.ALLERGY))
                        .build();
                userService.registerUser(cksdntjd);
                SignupRequestDto cksdntjd2 = SignupRequestDto.builder()
                        .username("cksdntjd2")
                        .password("cksdn123")
                        .nickname("chans")
                        .isVaccine(true)
                        .type(Type.AZ_PFIZER)
                        .degree(2)
                        .gender("남")
                        .age("28")
                        .disease("없음")
                        .afterEffect(Collections.singleton(AfterEffect.ALLERGY))
                        .build();
                userService.registerUser(cksdntjd2);
                User user = userRepository.findById(1L).orElseThrow(
                        ()-> new IllegalArgumentException("없는 유저입니다.")
                );
                User user2 = userRepository.findById(2L).orElseThrow(
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
                VacBoard vacBoard = VacBoard.builder()
                        .user(user2)
                        .title("user2의 title")
                        .contents("user2의 contents")
                        .build();
                vacBoardRepository.save(vacBoard);
            }
        };
    }
}
