package com.vaccinelife.vaccinelifeapi.controllerTest;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.CommentPostRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.Comment;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.enums.AfterEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.repository.CommentRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import com.vaccinelife.vaccinelifeapi.service.CommentService;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

class CommentControllerTest extends BaseControllerTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    UserRepository userRepository;

    @Test
    @TestDescription("댓글을 정상적으로 생성하는 테스트")
    void createComment() throws Exception {
        CommentPostRequestDto commentPostRequestDto = CommentPostRequestDto.builder()
                .vacBoardId(50L)
                .comment("커맨트 내용입니다.").build();
        commentService.createComment(commentPostRequestDto, 1L);

        this.mockMvc.perform(post("/api/comment")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(commentPostRequestDto))
        )
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.LOCATION))
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("vacBoardId").exists())
//                .andExpect(jsonPath("userId").exists())
//                .andExpect(jsonPath("comment").exists());

    }

    @Test
    @TestDescription("댓글을 정상적으로 삭제 테스트")
    void deleteComment() throws Exception {
        Long commentId = 1L;
        this.mockMvc.perform(delete("/api/comment/{commentId}",  commentId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @TestDescription("타인의 댓글을 삭제하려 하는 테스트")
    void not_athenticated_UserdeleteComment() throws Exception {
        Long commentId = 2L;
        this.mockMvc.perform(delete("/api/comment/{commentId}",  commentId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


//    private String another_getAccessToken() throws Exception {
//        String username = "cksdntjd2";
//        String password = "cksdn123";
//
//        Authentication authentication = new UserAuthentication(username, null, null);
//        String token = jwtTokenProvider.createToken(authentication);
//        Token.Response response = Token.Response.builder().token(token).build();
//
//        String res = String.valueOf(response);
//        String replace = res.substring(21, res.length() - 1);
//
//        return replace;
//    }
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
}