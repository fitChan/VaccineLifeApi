//package com.vaccinelife.vaccinelifeapi.config;
//
//import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
//import com.vaccinelife.vaccinelifeapi.model.Comment;
//import com.vaccinelife.vaccinelifeapi.model.User;
//import com.vaccinelife.vaccinelifeapi.model.VacBoard;
//import com.vaccinelife.vaccinelifeapi.model.enums.SideEffectname;
//import com.vaccinelife.vaccinelifeapi.model.enums.Type;
//import com.vaccinelife.vaccinelifeapi.repository.AfterEffectRepository;
//import com.vaccinelife.vaccinelifeapi.repository.CommentRepository;
//import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
//import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
//import com.vaccinelife.vaccinelifeapi.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.*;
//
//@Configuration
//public class AppConfig {
//
//
//    @Bean
//    public ApplicationRunner applicationRunner() {
//        return new ApplicationRunner() {
//            @Autowired
//            UserService userService;
//            @Autowired
//            VacBoardRepository vacBoardRepository;
//            @Autowired
//            CommentRepository commentRepository;
//            @Autowired
//            UserRepository userRepository;
//@Autowired
//            AfterEffectRepository afterEffectRepository;
//            //Test용 User.
//            @Override
//            public void run(ApplicationArguments args) throws Exception {
//                List<SideEffectname> sam = new ArrayList<>();
//                sam.add(SideEffectname.fatigue);
//                sam.add(SideEffectname.headache);
//                SignupRequestDto cksdntjd = SignupRequestDto.builder()
//                        .username("cksdntjd")
//                        .password("cksdn123")
//                        .nickname("chanoo")
//                        .isVaccine(true)
//                        .type(Type.AZ)
//                        .degree(2)
//                        .gender("남")
//                        .age("28")
//                        .disease("없음")
//                        .afterEffect(sam)
//                        .build();
//                userService.registerUser(cksdntjd);
//                List<SideEffectname> sam2 = new ArrayList<>();
//                sam.add(SideEffectname.fatigue);
//                sam.add(SideEffectname.fever);
//                SignupRequestDto cksdntjd2 = SignupRequestDto.builder()
//                        .username("cksdntjd2")
//                        .password("cksdn123")
//                        .nickname("chans")
//                        .isVaccine(true)
//                        .type(Type.AZ_PFIZER)
//                        .degree(2)
//                        .gender("남")
//                        .age("28")
//                        .disease("없음")
//                        .afterEffect(sam2)
//                        .build();
//                userService.registerUser(cksdntjd2);
//                User user = userRepository.findById(1L).orElseThrow(
//                        () -> new IllegalArgumentException("없는 유저입니다.")
//                );
//                User user2 = userRepository.findById(2L).orElseThrow(
//                        () -> new IllegalArgumentException("없는 유저입니다.")
//                );
//                for (int i = 0; i < 50; i++) {
//                    if (i % 2 == 0) {
//                        VacBoard vacBoard = VacBoard.builder()
//                                .user(user)
//                                .title("user1의 the title" + i + "번째 for Junit Test")
//                                .contents("the content" + i + "번째 for Junit Test")
//                                .build();
//                        vacBoardRepository.save(vacBoard);
//                    } else {
//                        VacBoard vacBoard = VacBoard.builder()
//                                .user(user2)
//                                .title("user2의 the title" + i + "번째 for Junit Test")
//                                .contents("the content" + i + "번째 for Junit Test")
//                                .build();
//                        vacBoardRepository.save(vacBoard);
//                    }
//                }
//
//                for (long i = 0L; i < 50L; i++) {
//                    VacBoard byId = vacBoardRepository.findById(i+1L).orElseThrow(
//                            ()-> new IllegalArgumentException("??????????????????????????")
//                    );
//                    if (i % 2 == 0) {
//                        Comment comment = Comment.builder()
//                                .user(user)
//                                .vacBoard(byId)
//                                .comment("user1의 contents")
//                                .build();
//                        commentRepository.save(comment);
//                    }else{
//                        Comment comment = Comment.builder()
//                                .user(user2)
//                                .vacBoard(byId)
//                                .comment("user2의 contents")
//                                .build();
//                        commentRepository.save(comment);
//                    }
//                    int commentCount = byId.getCommentCount();
//                    commentCount+= 1;
//                    byId.setCommentCount(commentCount);
//                }
//
//
////                1. 우선 유저 아이디로 user 받음.
////                2. 해당 유저를 통해 repo찾고 리스트 반환
////
////                User cksdntjd1 = userRepository.findByUsername("cksdntjd").orElseThrow(
////                        ()-> new IllegalArgumentException("not")
////                );
////                List<AfterEffect> allByUser = afterEffectRepository.findAllByUser(cksdntjd1);
////                for(int i=0; i<allByUser.size(); i++){
////                    System.out.println(allByUser.get(i).getSideEffectname());
////                }
//            }
//        };
//    }
//}
