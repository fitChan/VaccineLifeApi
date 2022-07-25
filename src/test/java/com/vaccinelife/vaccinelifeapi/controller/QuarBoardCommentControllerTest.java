package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.accountUser.WithUser;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.QuarCommentPostRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.QuarCommentRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import com.vaccinelife.vaccinelifeapi.service.QuarCommentService;
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

@WithUser
class QuarBoardCommentControllerTest extends BaseControllerTest {

    @Autowired
    QuarCommentRepository quarCommentRepository;
    @Autowired
    QuarCommentService quarCommentService;
    @Autowired
    UserRepository userRepository;

    @Test
    @TestDescription("QuarBoard 에 댓글을 정상적으로 생성하는 테스트")
    void create_quarBoard_comment() throws Exception {
        QuarCommentPostRequestDto commentPostRequestDto = QuarCommentPostRequestDto.builder()
                .quarBoardId(50L)
                .quarcomment("커맨트 내용입니다.").build();
        quarCommentService.createQuarComment(commentPostRequestDto, 1L);

        this.mockMvc.perform(post("/api/quarcomment")
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

                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("quarcomment").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.create-quarBoard-comment").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("quarBoard-comment-create",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("create-quarBoard-comment").description("link to create comment in specific quarBoard"),
                                linkWithRel("profile").description("profile of the [Post]create comment in specific quarBoard")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type Header")
                        ),
                        requestFields(
                                fieldWithPath("quarBoardId").description("the specific quarBoard Id from which the comment is generated"),
                                fieldWithPath("quarcomment").description("the comment content")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("id").description("the id of the posted comment in specific quarBoard"),
                                fieldWithPath("quarcomment").description("comment contents in the specific quarBoard"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.create-quarBoard-comment.href").description("link to comment in specific quarBoard"),
                                fieldWithPath("_links.profile.href").description("profile")
                        )

                ))

        ;

    }

    @Test
    @TestDescription("댓글을 정상적으로 삭제 테스트")
    void deleteComment() throws Exception {
        Long commentId = 1L;
        this.mockMvc.perform(delete("/api/quarcomment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @TestDescription("타인의 댓글을 삭제하려 하는 테스트")
    void not_athenticated_UserdeleteComment() throws Exception {
        Long commentId = 3L;
        this.mockMvc.perform(delete("/api/quarcomment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())

        ;
    }

    @Test
    @TestDescription("특정 게시판에 달린 댓글List Query")
    void comment_List_in_quarBoard() throws Exception {
        Long quarBoardId = 2L;
        this.mockMvc.perform(get("/api/quarBoard/{quarBoardId}/comments", quarBoardId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.quarCommentRequestDtoList[0].id").exists())
                .andExpect(jsonPath("_embedded.quarCommentRequestDtoList[0].quarBoardId").exists())
                .andExpect(jsonPath("_embedded.quarCommentRequestDtoList[0].quarcomment").exists())
                .andExpect(jsonPath("_embedded.quarCommentRequestDtoList[0].nickname").exists())
                .andExpect(jsonPath("_embedded.quarCommentRequestDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.quarCommentRequestDtoList[0].modifiedAt").exists())
                .andDo(document("quarBoard-comment-query-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type Header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.quarCommentRequestDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.quarCommentRequestDtoList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.quarCommentRequestDtoList[0].id").description("the id of the posted comment"),
                                fieldWithPath("_embedded.quarCommentRequestDtoList[0].quarBoardId").description("the id of the quarBaordId which is posted comment"),
                                fieldWithPath("_embedded.quarCommentRequestDtoList[0].quarcomment").description("comment of the specific quarBoard"),
                                fieldWithPath("_embedded.quarCommentRequestDtoList[0].nickname").description("nickname of the user who posted the comment"),
                                fieldWithPath("_links.self.href").description("link to self in quarBoard"),
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