package com.vaccinelife.vaccinelifeapi.controllerTest;

import com.sun.xml.bind.v2.TODO;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.enums.AfterEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import com.vaccinelife.vaccinelifeapi.service.VacBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private String getAccessToken() throws Exception {
        String username = "cksdntjd";
        String password = "cksdn123";
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("없는 유저 ")
        );
        Authentication authentication = new UserAuthentication(user.getId(), null, null);

        String token = jwtTokenProvider.createToken(authentication.getPrincipal().toString());
        Token.Response response = Token.Response.builder().token(token).build();

        String res = String.valueOf(response);
        String replace = res.substring(21, res.length()-1);

        return replace;
    }


    @Test
    @TestDescription("마이페이지에서 정상적으로 vacBoard List를 조회")
    public void getMypageVacBoard() throws Exception {
        this.mockMvc.perform(get("/api/mypage/vacBoard")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(jsonPath("id").exists())
//                .andExpect(jsonPath("title").exists())
//                .andExpect(jsonPath("nickname").exists())
//                .andExpect(jsonPath("likeCount").exists())
//                .andExpect(jsonPath("totalVisitors").exists())
//                .andExpect(jsonPath("commentCount").exists())
//                .andExpect(jsonPath("type").exists())
//                .andExpect(jsonPath("_links.href").exists())

         /*TODO List 조회이므로 body 확인 할것. */
        ;
    }

    @Test
    @TestDescription("마이페이지에서 정상적으로 quarBoard List를 조회")
    public void getMypageQuarBoard() throws Exception {
        this.mockMvc.perform(get("/api/mypage/quarBoard")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @TestDescription("마이페이지에서 정상적으로 medical List를 조회")
    public void getMypageMedical() throws Exception {
        this.mockMvc.perform(get("/api/mypage/medical")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
