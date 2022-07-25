package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.QuarBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.repository.QuarBoardRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QuarBoardLikeControllerTest extends BaseControllerTest {
    @Autowired
    QuarBoardRepository quarBoardRepository;
    @Autowired
    UserRepository userRepository;

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

        this.mockMvc.perform(post("/api/quarBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(quarBoardLikeRequestDto)));
        return quarBoardLikeRequestDto;
    }

    @Test
    @TestDescription("quarBoard의 like가 정상적으로 오르는 TEST")
    public void quarBoard의_like가_정상적으로_오르는_TEST() throws Exception {
        Long quarBoardId = 25L;
        QuarBoard quarBoard = quarBoardRepository.findById(quarBoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );
        QuarBoardLikeRequestDto quarBoardLikeRequestDto = QuarBoardLikeRequestDto.builder()
                .quarBoardId(quarBoard.getId())
                .likeCount(quarBoard.getLikeCount())
                .createdAt(quarBoard.getCreatedAt())
                .modifiedAt(quarBoard.getModifiedAt())
                .build();

        this.mockMvc.perform(post("/api/quarBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(quarBoardLikeRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value("true"))
                .andExpect(jsonPath("msg").value("quarBoard like is applied"))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("like_quarBoard_applied",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("quarBoardId").description("id(PK) of the quarBoard"),
                                fieldWithPath("likeCount").description("number of likeCount on the quarBoard"),
                                fieldWithPath("createdAt").description("createdAt date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("ok").description("false : canceled , ture : applied"),
                                fieldWithPath("msg").description("result information massage"),
                                fieldWithPath("status").description("the status of the response")
                        )
                ))

        ;
    }

    @Test
    @TestDescription("quarBoard의 like가 취소되는 TEST")
    public void quarBoard의_like가_취소되는_TEST() throws Exception {
        Long quarBoardId = 7L;
        QuarBoardLikeRequestDto quarBoardLikeRequestDto = pressLike(quarBoardId);
        this.mockMvc.perform(post("/api/quarBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(quarBoardLikeRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value("false"))
                .andExpect(jsonPath("msg").value("quarBoard like is canceled"))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("like_quarBoard_canceled",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("quarBoardId").description("id(PK) of the quarBoard"),
                                fieldWithPath("likeCount").description("number of likeCount on the quarBoard"),
                                fieldWithPath("createdAt").description("createdAt date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("ok").description("false : canceled , ture : applied"),
                                fieldWithPath("msg").description("result information massage"),
                                fieldWithPath("status").description("the status of the response")
                        )
                        ))
        ;
    }


    @Test
    @TestDescription("유저가 좋아요 누른 글의 목록 가져오기")
    public void 유저가_좋아요_누른_글의_목록_가져오기() throws Exception {
        Long quarBoardId = 1L;
        Long quarBoardId2 = 2L;
        pressLike(quarBoardId);
        pressLike(quarBoardId2);

        this.mockMvc.perform(get("/api/quarBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())


        ;
    }

}