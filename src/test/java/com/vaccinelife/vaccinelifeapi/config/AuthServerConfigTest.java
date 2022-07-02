package com.vaccinelife.vaccinelifeapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.enums.AfterEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.sound.sampled.DataLine;
import java.util.Collections;
import java.util.EnumSet;

import static com.vaccinelife.vaccinelifeapi.model.enums.AfterEffect.FATIGUEPAIN;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        EnumSet<AfterEffect> set = EnumSet.of(AfterEffect.ALLERGY, AfterEffect.FATIGUEPAIN);
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
    public void getAuthToken() throws Exception {
        String username = "cksdntjd";
        String password = "cksdn123";

        String s = objectMapper.writeValueAsString(new User(username, password));

        this.mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(s)
        )
                .andExpect(jsonPath("token").exists());

    }
}