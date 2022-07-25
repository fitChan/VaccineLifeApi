package com.vaccinelife.vaccinelifeapi.controller;


import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.QuarBoardRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.Authentication;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class QuarBoardControllerTest extends BaseControllerTest {


    @Autowired
    QuarBoardRepository quarBoardRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    @TestDescription("정상적으로 quarBoard를 생성함")
    public void quarBoard를_생성() throws Exception {
        QuarBoardPostRequestDto quarBoardRequestDto = QuarBoardPostRequestDto.builder()
                .title("the title 정상적으로 quarBoard를 생성함")
                .contents("the content 정상적으로 quarBoard를 생성함")
                .build();

        this.mockMvc.perform(post("/api/quarBoard")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(quarBoardRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("contents").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-quarBoards").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("quarBoard-create",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-quarBoards").description("link to query quarBoards"),
                                linkWithRel("profile").description("profile of the [Post]create quarBoard")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("title").description("title of the quarBoards"),
                                fieldWithPath("contents").description("contests of the quarBoards")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("id").description("the id of the quarBoards post"),
                                fieldWithPath("title").description("title of the quarBoards"),
                                fieldWithPath("contents").description("content of the quarBoards"),
                                fieldWithPath("totalVisitors").description("number of Visitors of the quarBoards"),
                                fieldWithPath("commentCount").description("number of comments on the quarBoards"),
                                fieldWithPath("likeCount").description("number of likeCount on the quarBoards"),
                                fieldWithPath("contents").description("contents of the quarBoards"),
                                fieldWithPath("_links.self.href").description("link to the particular quarBoard"),
                                fieldWithPath("_links.query-quarBoards.href").description("link to the quarBoards"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )

                ))
        ;
    }


    @Test
    @TestDescription("입력값이 없을경우 QuarBoard 생성 오류")
    public void createQuarBoard_Empty_Request() throws Exception {

        QuarBoardPostRequestDto quarBoard = QuarBoardPostRequestDto.builder().build();

        this.mockMvc.perform(post("/api/quarBoard")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(quarBoard)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    @TestDescription("30개의 게시물을 10개씩 페이지 조회하기.")
    public void queryQuarBoardList() throws Exception {
        this.mockMvc.perform(get("/api/quarBoard")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.quarBoardList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("quarBoard-query-List",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("first").description("link to quarBoard"),
                                linkWithRel("prev").description("link to previous quarBoard page"),
                                linkWithRel("next").description("link to next quarBoard page"),
                                linkWithRel("last").description("link to last quarBoard"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("_embedded.quarBoardList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.quarBoardList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.quarBoardList[0].id").description("the id of the quarBoard post"),
                                fieldWithPath("_embedded.quarBoardList[0].title").description("title of the quarBoard"),
                                fieldWithPath("_embedded.quarBoardList[0].contents").description("content of the quarBoard"),
                                fieldWithPath("_embedded.quarBoardList[0].totalVisitors").description("number of Visitors of the quarBoard"),
                                fieldWithPath("_embedded.quarBoardList[0].commentCount").description("number of comments on the quarBoard"),
                                fieldWithPath("_embedded.quarBoardList[0].likeCount").description("number of likeCount on the quarBoard")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("quarBoard 게시물 하나를 조회하는 테스트")
    public void get_quarBoard() throws Exception {
        QuarBoard quarBoard = quarBoardRepository.findById(2L).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        this.mockMvc.perform(get("/api/quarBoard/{quarBoardId}", quarBoard.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("contents").exists())
                .andExpect(jsonPath("totalVisitors").exists())
                .andExpect(jsonPath("likeCount").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("commentCount").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("quarBoard-query",
//                        links(
//                                linkWithRel("_links.self.href").description("link to self"),
//                                linkWithRel("_links.profile.href").description("link to profile")
//                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type Header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("id").description("the id of the quarBoard"),
                                fieldWithPath("title").description("title of the quarBoard"),
                                fieldWithPath("contents").description("content of the quarBoard"),
                                fieldWithPath("totalVisitors").description("number of Visitors of the quarBoard"),
                                fieldWithPath("likeCount").description("number of like on the quarBoard"),
                                fieldWithPath("nickname").description("nickname of the user who posted the quarBoard"),
                                fieldWithPath("commentCount").description("number of comment on the quarBoard"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;
    }


    @Test
    @TestDescription("게시물을 수정하는 테스트")
    public void update_quarBoard() throws Exception {
        Long id = 3L;
        QuarBoard quarBoard = quarBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("없는 게시물입니다.")
        );
        quarBoard.update(QuarBoardRequestDto.builder()
                .title("updated title")
                .contents("updated contents")
                .build());


        this.mockMvc.perform(put("/api/quarBoard/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(quarBoard)))
                .andDo(print())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("title").value("updated title"))
                .andExpect(jsonPath("contents").value("updated contents"))
                .andExpect(jsonPath("modifiedAt").exists())
                .andDo(document("quarBoard-update",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("update-quarBoard").description("link to updated quarBoard"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("id").description("id(PK) of the quarBoard"),
                                fieldWithPath("title").description("updated title of the quarBoard"),
                                fieldWithPath("contents").description("updated contents of the quarBoard"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("totalVisitors").description("number of Visitors of the quarBoard"),
                                fieldWithPath("likeCount").description("number of like on the quarBoard"),
                                fieldWithPath("commentCount").description("number of comment on the quarBoard")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("id").description("id(PK) of the quarBoard"),
                                fieldWithPath("title").description("title of the quarBoard"),
                                fieldWithPath("contents").description("content of the quarBoard"),
                                fieldWithPath("totalVisitors").description("number of Visitors of the quarBoard"),
                                fieldWithPath("likeCount").description("number of like on the quarBoard"),
                                fieldWithPath("commentCount").description("number of comment on the quarBoard"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.update-quarBoard.href").description("link to quarBoard List"),
                                fieldWithPath("_links.profile.href").description("link to profile")

                        )
                ))
        ;
    }


    @Test
    @TestDescription("타인의 게시물을 수정하는 테스트")
    public void update_quarBoard_access_deny() throws Exception {

        Long id = 2L;
        QuarBoard quarBoard = quarBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("없는 게시물입니다.")
        );
        quarBoard.update(QuarBoardRequestDto.builder()
                .title("updated title")
                .contents("updated contents")
                .build());


        this.mockMvc.perform(put("/api/quarBoard/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("좋아요Top3 게시물 가져오는 테스트")
    public void 좋아요Top3_게시물_가져오기() throws Exception {
        pressLike(4L);
        pressLike(5L);

        this.mockMvc.perform(get("/api/quarBoard/topLike")
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].quarBoardId").exists())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].title").exists())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].contents").exists())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].totalVisitors").exists())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].commentCount").exists())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.quarBoardTopRequestDtoList[0].modifiedAt").exists())

        .andDo(document("quarBoard-query-top3",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                ),
                responseFields(
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].createdAt").description("created date and time"),
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].modifiedAt").description("modified date and time"),
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].quarBoardId").description("id(PK) of the quarBoard"),
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].title").description("title of the quarBoard"),
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].contents").description("content of the quarBoard"),
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].totalVisitors").description("number of Visitors of the quarBoard"),
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].likeCount").description("number of like on the quarBoard"),
                        fieldWithPath("_embedded.quarBoardTopRequestDtoList[0].commentCount").description("number of comment on the quarBoard")

                )
                ))

        ;
    }

    private String getAccessToken() throws Exception {
        String username = "cksdntjd";

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("없는 유저 ")
        );
        Authentication authentication = new UserAuthentication(user.getId(), null, null);

        String token = jwtTokenProvider.createToken(authentication.getPrincipal().toString());
        Token.Response response = Token.Response.builder().token(token).build();

        String res = String.valueOf(response);

        return res.substring(21, res.length() - 1);
    }

    public QuarBoardLikeRequestDto pressLike(Long quarBoardId) throws Exception {
        QuarBoard quarBoard = quarBoardRepository.findById(quarBoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );

        QuarBoardLikeRequestDto quarBoardLikeRequestDto = QuarBoardLikeRequestDto.builder()
                .quarBoardId(quarBoard.getId())
                .likeCount(quarBoard.getLikeCount())
                .createdAt(quarBoard.getCreatedAt())
                .modifiedAt(quarBoard.getModifiedAt())
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/quarBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(quarBoardLikeRequestDto)));
        return quarBoardLikeRequestDto;
    }
}
