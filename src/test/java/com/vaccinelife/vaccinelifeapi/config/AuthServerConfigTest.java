package com.vaccinelife.vaccinelifeapi.config;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    UserService userService;

    @Test
    @TestDescription("인증 토큰 발급 테스트")
    public void getAuthToken() throws Exception {
        String username = "cksdntjd123";
        String password = "cksdn123";
        SignupRequestDto cksdntjd = SignupRequestDto.builder()
                .username(username)
                .password(password)
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
        String userId = "myApp";
        String userSecret = "pass";

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(userId, userSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}