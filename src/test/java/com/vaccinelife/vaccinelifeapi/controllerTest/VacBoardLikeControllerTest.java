package com.vaccinelife.vaccinelifeapi.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
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

class VacBoardLikeControllerTest extends BaseControllerTest {
    @Autowired
    VacBoardRepository vacBoardRepository;
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

    public VacBoardLikeRequestDto pressLike(Long vacBoardId) throws Exception {
        VacBoard vacBoard = vacBoardRepository.findById(vacBoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );

        VacBoardLikeRequestDto vacBoardLikeRequestDto = VacBoardLikeRequestDto.builder()
                .vacBoardId(vacBoard.getId())
                .likeCount(vacBoard.getLikeCount())
                .createdAt(vacBoard.getCreatedAt())
                .modifiedAt(vacBoard.getModifiedAt())
                .build();

        this.mockMvc.perform(post("/api/vacBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(vacBoardLikeRequestDto)));
        return vacBoardLikeRequestDto;
    }

    @Test
    @TestDescription("vacBoard의 like가 정상적으로 오르는 TEST")
    public void vacBoard의_like가_정상적으로_오르는_TEST() throws Exception {
        Long vacBoardId = 1L;
        VacBoard vacBoard = vacBoardRepository.findById(vacBoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );
        VacBoardLikeRequestDto vacBoardLikeRequestDto = VacBoardLikeRequestDto.builder()
                .vacBoardId(vacBoard.getId())
                .likeCount(vacBoard.getLikeCount())
                .createdAt(vacBoard.getCreatedAt())
                .modifiedAt(vacBoard.getModifiedAt())
                .build();

        this.mockMvc.perform(post("/api/vacBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(vacBoardLikeRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value("true"))
                .andExpect(jsonPath("msg").value("vacBoard like is applied"))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("like_vacBoard_applied",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("vacBoardId").description("id(PK) of the vacBoard"),
                                fieldWithPath("likeCount").description("number of likeCount on the vacBoard"),
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
    @TestDescription("vacBoard의 like가 취소되는 TEST")
    public void vacBoard의_like가_취소되는_TEST() throws Exception {
        Long vacBoardId = 1L;
        VacBoardLikeRequestDto vacBoardLikeRequestDto = pressLike(vacBoardId);
        this.mockMvc.perform(post("/api/vacBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(vacBoardLikeRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value("false"))
                .andExpect(jsonPath("msg").value("vacBoard like is canceled"))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("like_vacBoard_canceled",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("vacBoardId").description("id(PK) of the vacBoard"),
                                fieldWithPath("likeCount").description("number of likeCount on the vacBoard"),
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
        Long vacBoardId = 1L;
        Long vacBoardId2 = 2L;
        pressLike(vacBoardId);
        pressLike(vacBoardId2);

        this.mockMvc.perform(get("/api/vacBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())


        ;
    }

}