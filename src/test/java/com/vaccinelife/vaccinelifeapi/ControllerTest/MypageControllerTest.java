package com.vaccinelife.vaccinelifeapi.ControllerTest;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import com.vaccinelife.vaccinelifeapi.service.VacBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MypageControllerTest extends BaseControllerTest {

    @Autowired
    VacBoardRepository vacBoardRepository;
    @Autowired
    VacBoardService vacBoardService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

//    private String getAccessToken() throws Exception {
//        String username = "cksdntjd";
//        String password = "cksdn123";
//        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
//                .username(username)
//                .password(password)
//                .build();
//        this.mockMvc.perform(post("/api/signup"));
//        String userId = "myApp";
//        String userSecret = "pass";
//
//        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
//                .with(httpBasic(userId, userSecret))
//                .param("username", username)
//                .param("password", password)
//                .param("grant_type", "password")
//        );
//        var responseBody = perform.andReturn().getResponse().getContentAsString();
//        ObjectMapper parse = new ObjectMapper();
//        return parse.parseMap(responseBody).get("access_token").toString();
//        parse.readValue(responseBody)
//    } objectMapper 사용하기


    private String createToken() throws Exception {
        String username = "cksdntjd";
        String password = "cksdn123";


        ResultActions perform = this.mockMvc.perform(post("/api/signup")
                .param("username", username)
                .param("password", password)
        );
        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString+"--------------------------------------------------------------------");
        return contentAsString;
    }

    @Test
    @TestDescription("마이페이지에서 정상적으로 vacBoard List를 조회")
    public void getMypageVacBoard() throws Exception {

        User user = userRepository.findByUsername("cksdntjd").orElseThrow(
                () -> new IllegalArgumentException("없는 유저")
        );

        this.mockMvc.perform(get("/api/mypage/vacBoard")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .header("X-AUTH-TOKEN", createToken())
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }
}
