package com.vaccinelife.vaccinelifeapi.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.UserRole;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.NotBlank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends BaseControllerTest {


    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    public void 회원가입(){
        SignupRequestDto user = new SignupRequestDto();
        user.setId(1L);
        user.setUsername("아이디");
        user.setPassword("cksdn123");
        user.setPasswordChecker("cksdn123");
        user.setNickname("닉네임");
        user.setIsVaccine(true);
        user.setType("화이자");
        user.setDegree(2);
        user.setGender("남");
        user.setAge("28");
        user.setDisease("없음");
        user.setAfterEffect("인후통");

        userService.registerUser(user);

        User findUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
        assertThat(user.getId()).isEqualTo(findUser.getId());
        assertThat(user.getPassword()).isEqualTo(user.getPasswordChecker());
        assertThat(user.getUsername()).isEqualTo(findUser.getUsername());
        assertThat(user.getNickname()).isEqualTo(findUser.getNickname());
        assertThat(user.getIsVaccine()).isEqualTo(findUser.getIsVaccine());
        assertThat(user.getType()).isEqualTo(findUser.getType());
        assertThat(user.getDegree()).isEqualTo(findUser.getDegree());
        assertThat(user.getGender()).isEqualTo(findUser.getGender());
        assertThat(user.getAge()).isEqualTo(findUser.getAge());
        assertThat(user.getDisease()).isEqualTo(findUser.getDisease());
        assertThat(user.getAfterEffect()).isEqualTo(findUser.getAfterEffect());
    }


//    @Test
//    public void 회원가입비밀번호확인오류() throws Exception{
//        SignupRequestDto user = new SignupRequestDto();
//        user.setId(1L);
//        user.setUsername("아이디");
//        user.setPassword("cksdn123");
//        user.setPasswordChecker("cksdn124");
//        user.setNickname("닉네임");
//        user.setIsVaccine(true);
//        user.setType("화이자");
//        user.setDegree(2);
//        user.setGender("남");
//        user.setAge("28");
//        user.setDisease("없음");
//        user.setAfterEffect("인후통");
//
//        userService.registerUser(user);
//
//        mockMvc.perform(post("/api/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
//                .content(objectMapper.writeValueAsString(user)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }

}
