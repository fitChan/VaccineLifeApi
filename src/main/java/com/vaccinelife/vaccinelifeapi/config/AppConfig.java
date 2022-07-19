package com.vaccinelife.vaccinelifeapi.config;

import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.*;
import com.vaccinelife.vaccinelifeapi.model.enums.SideEffectname;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.repository.*;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

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
            CommentRepository commentRepository;
            @Autowired
            MedicalRepository medicalRepository;
            @Autowired
            QuarBoardRepository quarBoardRepository;
            @Autowired
            UserRepository userRepository;
            @Autowired
            SideEffectRepository sideEffectRepository;
            @Autowired
            QuarCommentRepository quarCommentRepository;

            //Test용 User.
            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<SideEffectname> sam = new ArrayList<>();
                sam.add(SideEffectname.fatigue);
                sam.add(SideEffectname.headache);
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
                        .afterEffect(sam)
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
                        .afterEffect(sam)
                        .build();
                userService.registerUser(cksdntjd2);
                User user = userRepository.findById(1L).orElseThrow(
                        () -> new IllegalArgumentException("없는 유저입니다.")
                );
                User user2 = userRepository.findById(2L).orElseThrow(
                        () -> new IllegalArgumentException("없는 유저입니다.")
                );
                for (int i = 0; i < 50; i++) {
                    if (i % 2 == 0) {
                        VacBoard vacBoard = VacBoard.builder()
                                .user(user)
                                .title("user1의 the title" + i + "번째 for Junit Test")
                                .contents("the content" + i + "번째 for Junit Test")
                                .build();

                        QuarBoard quarBoard = QuarBoard.builder()
                                .user(user)
                                .title("user1의"+i+"번 째 QuarBoard for Junit Test")
                                .contents("user1의"+i+"번 째 QuarBoard for Junit Test")
                                .build();

                        Medical medical = Medical.builder()
                                .user(user)
                                .contents("user1의"+i+"번 째 Medical for Junit Test")
                                .build();

                        vacBoardRepository.save(vacBoard);
                        quarBoardRepository.save(quarBoard);
                        medicalRepository.save(medical);
                    } else {
                        VacBoard vacBoard = VacBoard.builder()
                                .user(user2)
                                .title("user2의 the title" + i + "번째 for Junit Test")
                                .contents("the content" + i + "번째 for Junit Test")
                                .build();

                        QuarBoard quarBoard = QuarBoard.builder()
                                .user(user2)
                                .title("user1의"+i+"번 째 QuarBoard for Junit Test")
                                .contents("user1의"+i+"번 째 QuarBoard for Junit Test")
                                .build();

                        Medical medical = Medical.builder()
                                .user(user2)
                                .contents("user1의"+i+"번 째 Medical for Junit Test")
                                .build();
                        vacBoardRepository.save(vacBoard);
                        quarBoardRepository.save(quarBoard);
                        medicalRepository.save(medical);
                    }

                }

                for (long i = 0L; i < 50L; i++) {
                    VacBoard byId = vacBoardRepository.findById(i + 1L).orElseThrow(
                            () -> new IllegalArgumentException("??????????????????????????")
                    );
                    QuarBoard quarBoard = quarBoardRepository.findById(i+1L).orElseThrow(
                            () -> new IllegalArgumentException("??????????????????????????")
                    );
                    for (int j = 0; j < 2; j++) {
                        if (i % 2 == 0) {
                            Comment comment = Comment.builder()
                                    .user(user)
                                    .vacBoard(byId)
                                    .comment("user1의 contents")
                                    .build();
                            QuarComment quarComment = QuarComment.builder()
                                    .user(user)
                                    .quarBoard(quarBoard)
                                    .quarcomment("user1의 contents")
                                    .build();
                            commentRepository.save(comment);
                            quarCommentRepository.save(quarComment);
                        } else {
                            Comment comment = Comment.builder()
                                    .user(user2)
                                    .vacBoard(byId)
                                    .comment("user2의 contents")
                                    .build();
                            QuarComment quarComment = QuarComment.builder()
                                    .user(user2)
                                    .quarBoard(quarBoard)
                                    .quarcomment("user2의 contents")
                                    .build();
                            quarCommentRepository.save(quarComment);
                            commentRepository.save(comment);
                        }

                        int commentCount = byId.getCommentCount();
                        commentCount += 1;
                        int quarCommentCount = quarBoard.getCommentCount();
                        quarCommentCount+=1;
                        byId.setCommentCount(commentCount);
                        quarBoard.setCommentCount(quarCommentCount);
                    }
                }
            }
        };
    }
}
