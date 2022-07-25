package com.vaccinelife.vaccinelifeapi.controllerTest;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.MedicalLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.MedicalRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.MedicalTop3RequestDto;
import com.vaccinelife.vaccinelifeapi.dto.QuarBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.User;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MedicalControllerTest extends BaseControllerTest {

    @Autowired
    MedicalRepository medicalRepository;

    @Test
    @TestDescription("매디컬 리스트를 가져오는 테스트")
    public void 매디컬_리스트를_가져오는_테스트() throws Exception {

        this.mockMvc.perform(get("/api/medical")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].id").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].contents").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].nickname").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].modifiedAt").exists())

                .andDo(document("medical-query-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.medicalResponseDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].id").description("the id of the medical post"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].contents").description("content of the medical post"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].nickname").description("nickname of the user who posted the medical"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].likeCount").description("number of likeCount on the medical post")
                        )
                ))

        ;
    }

    @Test
    @TestDescription("좋아요 top3를 불러오는 테스트")
    void getTopList() throws Exception {
        pressLike(4L);
        pressLike(5L);

        this.mockMvc.perform(get("/api/medical/topLike")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.medicalTop3RequestDtoList[0].id").exists())
                .andExpect(jsonPath("_embedded.medicalTop3RequestDtoList[0].contents").exists())
                .andExpect(jsonPath("_embedded.medicalTop3RequestDtoList[0].nickname").exists())
                .andExpect(jsonPath("_embedded.medicalTop3RequestDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.medicalTop3RequestDtoList[0].createdAt").exists())

                .andDo(document("medical-query-top3",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.medicalTop3RequestDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.medicalTop3RequestDtoList[0].id").description("the id of the medical post"),
                                fieldWithPath("_embedded.medicalTop3RequestDtoList[0].contents").description("content of the medical"),
                                fieldWithPath("_embedded.medicalTop3RequestDtoList[0].likeCount").description("number of likeCount on the medical"),
                                fieldWithPath("_embedded.medicalTop3RequestDtoList[0].nickname").description("the nickname of the user who posted the medical")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("메디컬을 작성하는 테스트")
    void 메디컬을_작성하는_테스트() throws Exception {
        MedicalRequestDto medicalRequestDto = MedicalRequestDto.builder()
                .contents("medical 작성 테스트용 post")
                .build();


        this.mockMvc.perform(post("/api/medical")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(medicalRequestDto))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("ok").exists())
                .andExpect(jsonPath("msg").exists())
                .andExpect(jsonPath("status").exists())
                .andDo(document("medical-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("contents").description("the content of the medical post")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("ok").description("true : saved"),
                                fieldWithPath("msg").description("result information massage"),
                                fieldWithPath("status").description("the status of the response")
                        )
                ))

        ;
    }

    @Test
    @TestDescription("매디컬을 삭제하는 테스트")
    void deleteMedical() throws Exception {

        this.mockMvc.perform(delete("/api/medical/" + 3L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @TestDescription("다른 유저가 메디컬을 삭제하는 테스트")
    void not_athenticated_user_deleteMedical() throws Exception {

        this.mockMvc.perform(delete("/api/medical/" + 2L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("매디컬을 수정하는 테스트")
    void patchVacBoard() throws Exception {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("contents", "new contents updated");

        this.mockMvc.perform(patch("/api/medical/" + 3L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(fields))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ok").exists())
                .andExpect(jsonPath("msg").exists())
                .andExpect(jsonPath("status").exists())
        ;
    }

    @Test
    @TestDescription("다른 유저가 매디컬을 수정하는 테스트")
    void not_athenticated_user_patchVacBoard() throws Exception {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("contents", "new contents updated");

        this.mockMvc.perform(patch("/api/medical/" + 2L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(fields))
        ).andDo(print())
                .andExpect(status().isBadRequest())

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

        MedicalLikeRequestDto medicalTop3RequestDto = MedicalLikeRequestDto.builder()
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
                .content(objectMapper.writeValueAsString(medicalTop3RequestDto)));
        return medicalTop3RequestDto;
    }
}