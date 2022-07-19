package com.vaccinelife.vaccinelifeapi.config;

import com.vaccinelife.vaccinelifeapi.accountUser.WithUser;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    //    @Test
//    @TestDescription("인증 토큰 발급 테스트")
//    public void getAuthToken() throws Exception {
//        String username = "cksdntjd123";
//        String password = "cksdn123";
//
//        SignupRequestDto cksdntjd = SignupRequestDto.builder()
//                .username(username)
//                .password(password)
//                .nickname("chanoo")
//                .isVaccine(true)
//                .type(Type.PFIZER)
//                .degree(2)
//                .gender("남")
//                .age("28")
//                .disease("없음")
//                .afterEffect(set)
//                .build();
//        userService.registerUser(cksdntjd);
//        String userId = "myApp";
//        String userSecret = "pass";
//
//        this.mockMvc.perform(post("/auth/login")
//                .with(httpBasic(userId, userSecret))
//                .param("username", username)
//                .param("password", password)
//                .param("grant_type", "password")
//        )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("access_token").exists());
//    }
    @Test
    @TestDescription("인증 토큰 발급 테스트")
    public void 인증_토큰_발급_테스트() throws Exception {
        String username = "cksdntjd";
        String password = "cksdn123";

        String s = objectMapper.writeValueAsString(new Token.Request(username, password));

        this.mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(s)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").exists());

    }
}