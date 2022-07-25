package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.MedicalLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.MedicalRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MedicalLikeControllerTest extends BaseControllerTest {

    @Autowired
    MedicalRepository medicalRepository;
    @Test
    @TestDescription("medical 좋아요누르기")
    void medical_좋아요누르기() throws Exception{
        Medical medical = medicalRepository.findById(10L).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );

        MedicalLikeRequestDto meicalLikeRqDto = MedicalLikeRequestDto.builder()
                .medicalId(medical.getId())
                .contents(medical.getContents())
                .likeCount(medical.getLikeCount())
                .createdAt(medical.getCreatedAt())
                .modifiedAt(medical.getModifiedAt())
                .build();

        this.mockMvc.perform(post("/api/medical/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(meicalLikeRqDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value("true"))
                .andExpect(jsonPath("msg").value("medical 게시글 좋아요 추가"))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("like_medical_applied",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("medicalId").description("id(PK) of the medical"),
                                fieldWithPath("likeCount").description("number of like count on the medical"),
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
                        )))
        ;
    }

    @Test
    @TestDescription("medical 좋아요취소")
    void medical_좋아요취소() throws Exception{


        MedicalLikeRequestDto meicalLikeRqDto = pressLike(40L);


        this.mockMvc.perform(post("/api/medical/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(meicalLikeRqDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").value("false"))
                .andExpect(jsonPath("msg").value("medical 게시글 좋아요 취소"))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("like_medical_canceled",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("medicalId").description("id(PK) of the medical"),
                                fieldWithPath("likeCount").description("number of like count on the medical"),
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
                        )))
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

    public MedicalLikeRequestDto pressLike(Long medicalId) throws Exception {
        Medical medical = medicalRepository.findById(medicalId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );

        MedicalLikeRequestDto meicalLikeRqDto = MedicalLikeRequestDto.builder()
                .medicalId(medical.getId())
                .contents(medical.getContents())
                .likeCount(medical.getLikeCount())
                .createdAt(medical.getCreatedAt())
                .modifiedAt(medical.getModifiedAt())
                .build();

        this.mockMvc.perform(post("/api/medical/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(meicalLikeRqDto)));
        return meicalLikeRqDto;
    }
}