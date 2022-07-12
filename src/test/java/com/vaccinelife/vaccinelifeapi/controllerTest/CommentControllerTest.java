package com.vaccinelife.vaccinelifeapi.controllerTest;

import com.vaccinelife.vaccinelifeapi.accountUser.WithUser;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.CommentPostRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.CommentRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import com.vaccinelife.vaccinelifeapi.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@WithUser
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
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(commentPostRequestDto))
        )
                .andDo(print())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("comment").exists())
                .andDo(document("comment-create-in-vacBoard",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("create-comment").description("link to create comment"),
                                linkWithRel("profile").description("profile of the [Post]create vacboard")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type Header")
                        ),
                        requestFields(
                                fieldWithPath("vacBoardId").description("the specific vacBoardId from which the comment is generated"),
                                fieldWithPath("comment").description("the comment content")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("id").description("the id of the posted comment"),
                                fieldWithPath("comment").description("comment of the specific vacBoard"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.create-comment.href").description("link to comment in specific vacBoard"),
                                fieldWithPath("_links.profile.href").description("profile")
                        )

                ));

    }

    @Test
    @TestDescription("댓글을 정상적으로 삭제 테스트")
    void deleteComment() throws Exception {
        Long commentId = 1L;
        this.mockMvc.perform(delete("/api/comment/{commentId}", commentId)
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
        Long commentId = 3L;
        this.mockMvc.perform(delete("/api/comment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("특정 게시판에 달린 댓글List Query")
    void comment_List_in_vacBoard() throws Exception {
        Long vacBoardId = 2L;
        this.mockMvc.perform(get("/api/vacBoard/{vacBoardId}/comments", vacBoardId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.commentRequestDtoList[0].id").exists())
                .andExpect(jsonPath("_embedded.commentRequestDtoList[0].vacBoardId").exists())
                .andExpect(jsonPath("_embedded.commentRequestDtoList[0].comment").exists())
                .andExpect(jsonPath("_embedded.commentRequestDtoList[0].nickname").exists())
                .andExpect(jsonPath("_embedded.commentRequestDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.commentRequestDtoList[0].modifiedAt").exists())
                .andDo(document("comment-query-list-in-vacBoard",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type Header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.commentRequestDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.commentRequestDtoList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.commentRequestDtoList[0].id").description("the id of the posted comment"),
                                fieldWithPath("_embedded.commentRequestDtoList[0].vacBoardId").description("the id of the vacBaordId which is posted comment"),
                                fieldWithPath("_embedded.commentRequestDtoList[0].comment").description("comment of the specific vacBoard"),
                                fieldWithPath("_embedded.commentRequestDtoList[0].nickname").description("nickname of the user who posted the comment"),
                                fieldWithPath("_links.self.href").description("link to self in vacBoard"),
                                fieldWithPath("_links.profile.href").description("profile")
                        )

                        ))
        ;

    }


    private String getAccessToken() throws Exception {
        String username = "cksdntjd";
        String password = "cksdn123";
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("없는 유저 ")
        );
        Authentication authentication = new UserAuthentication(user.getId(), null, null);

        String token = jwtTokenProvider.createToken(authentication.getPrincipal().toString());
        Token.Response response = Token.Response.builder().token(token).build();

        String res = String.valueOf(response);
        String replace = res.substring(21, res.length() - 1);

        return replace;
    }
}