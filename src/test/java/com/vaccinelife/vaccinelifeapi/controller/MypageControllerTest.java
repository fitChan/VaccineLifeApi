package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.MedicalLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.QuarBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.MedicalRepository;
import com.vaccinelife.vaccinelifeapi.repository.QuarBoardRepository;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MypageControllerTest extends BaseControllerTest {

    @Autowired
    VacBoardRepository vacBoardRepository;
    @Autowired
    QuarBoardRepository quarBoardRepository;
    @Autowired
    MedicalRepository medicalRepository;
    @Autowired
    VacBoardService vacBoardService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;


    public MedicalLikeRequestDto medicalpressLike(Long medicalId) throws Exception {
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

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/medical/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(meicalLikeRqDto)));
        return meicalLikeRqDto;
    }

    public VacBoardLikeRequestDto vacBoardpressLike(Long vacBoardId) throws Exception {
        VacBoard vacBoard = vacBoardRepository.findById(vacBoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다.")
        );

        VacBoardLikeRequestDto vacBoardLikeRequestDto = VacBoardLikeRequestDto.builder()
                .vacBoardId(vacBoard.getId())
                .likeCount(vacBoard.getLikeCount())
                .createdAt(vacBoard.getCreatedAt())
                .modifiedAt(vacBoard.getModifiedAt())
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/vacBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(vacBoardLikeRequestDto)));
        return vacBoardLikeRequestDto;
    }
    public QuarBoardLikeRequestDto quarBoardpressLike(Long quarBoardId) throws Exception {
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


    @Test
    @TestDescription("마이페이지에서 정상적으로 vacBoard List를 조회")
    public void getMypageVacBoard() throws Exception {
        this.mockMvc.perform(get("/api/mypage/vacBoard")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0].id").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0].title").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0].nickname").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0].totalVisitors").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0].commentCount").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0].type").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0]._links.vacBoardLink.href").exists())
                .andExpect(jsonPath("_embedded.vacBoardSimRequestDtoList[0]._links.profile.href").exists())
                .andDo(document("mypage-vacBoard-List",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].id").description("the id of the vacBoard post"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].title").description("title of the vacBoard"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].nickname").description("nickname of the user who posted the vacBoard"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].totalVisitors").description("number of Visitors of the vacBoard"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].commentCount").description("number of comments on the vacBoard"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].likeCount").description("number of likeCount on the vacBoard"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0].type").description("the vaccine type of the user who posted the vacBoard"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0]._links.vacBoardLink.href").description("link to vacBoard self"),
                                fieldWithPath("_embedded.vacBoardSimRequestDtoList[0]._links.profile.href").description("profile")
                        )
                ))
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].quarBoardId").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].title").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].nickname").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].commentCount").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].totalVisitors").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].quarBoardId").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0].modifiedAt").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0]._links.quarBoardLink.href").exists())
                .andExpect(jsonPath("_embedded.quarBoardSimRequestDtoList[0]._links.profile.href").exists())

                .andDo(document("mypage-quarBoard-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].quarBoardId").description("the id of the quarBoard post"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].title").description("title of the quarBoard"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].nickname").description("nickname of the user who posted the quarBoard"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].likeCount").description("number of likeCount on the quarBoard"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].totalVisitors").description("number of Visitors of the quarBoard"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0].commentCount").description("number of comments on the quarBoard"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0]._links.quarBoardLink.href").description("link to quarBoard self"),
                                fieldWithPath("_embedded.quarBoardSimRequestDtoList[0]._links.profile.href").description("profile")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("마이페이지에서 정상적으로 medical List를 조회")
    public void getMypageMedical() throws Exception {
        this.mockMvc.perform(get("/api/mypage/medical")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].id").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].contents").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].nickname").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0].modifiedAt").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0]._links.medicalLink.href").exists())
                .andExpect(jsonPath("_embedded.medicalResponseDtoList[0]._links.profile.href").exists())
                .andDo(document("mypage-medical-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("_embedded.medicalResponseDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].id").description("the id of the medical post"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].nickname").description("nickname of the user who posted the medical"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0].likeCount").description("number of likeCount on the medical"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0]._links.medicalLink.href").description("link to medical self"),
                                fieldWithPath("_embedded.medicalResponseDtoList[0]._links.profile.href").description("profile")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("유저가 좋아요 누른 vacBoard 가져오기")
    public void 유저가_좋아요_누른_vacBoard_list_가져오기() throws Exception {

        vacBoardpressLike(5L);
        vacBoardpressLike(10L);
        vacBoardpressLike(19L);
        vacBoardpressLike(20L);
        vacBoardpressLike(50L);

        this.mockMvc.perform(get("/api/vacBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.vacBoardLikeRequestDtoList[0].vacBoardId").exists())
                .andExpect(jsonPath("_embedded.vacBoardLikeRequestDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.vacBoardLikeRequestDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.vacBoardLikeRequestDtoList[0].modifiedAt").exists())
                .andExpect(jsonPath("_embedded.vacBoardLikeRequestDtoList[0].title").exists())

        .andDo(document("mypage_vacBoard_like_list",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                ),
                responseFields(
                        fieldWithPath("_embedded.vacBoardLikeRequestDtoList[0].createdAt").description("created date and time"),
                        fieldWithPath("_embedded.vacBoardLikeRequestDtoList[0].modifiedAt").description("modified date and time"),
                        fieldWithPath("_embedded.vacBoardLikeRequestDtoList[0].vacBoardId").description("the id of the vacBoard post"),
                        fieldWithPath("_embedded.vacBoardLikeRequestDtoList[0].likeCount").description("number of likeCount on the vacBoard"),
                        fieldWithPath("_embedded.vacBoardLikeRequestDtoList[0].title").description("the title of the vacBoard")
                )

                ))
        ;
    }

    @Test
    @TestDescription("유저가 좋아요 누른 quarBoard 가져오기")
    public void 유저가_좋아요_누른_quarBoard_list_가져오기() throws Exception {

        quarBoardpressLike(5L);
        quarBoardpressLike(10L);
        quarBoardpressLike(19L);
        quarBoardpressLike(20L);
        quarBoardpressLike(50L);

        this.mockMvc.perform(get("/api/quarBoard/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.quarBoardLikeRequestDtoList[0].quarBoardId").exists())
                .andExpect(jsonPath("_embedded.quarBoardLikeRequestDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.quarBoardLikeRequestDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.quarBoardLikeRequestDtoList[0].modifiedAt").exists())
                .andExpect(jsonPath("_embedded.quarBoardLikeRequestDtoList[0].title").exists())

        .andDo(document("mypage_quarBoard_like_list",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                ),
                responseFields(
                        fieldWithPath("_embedded.quarBoardLikeRequestDtoList[0].createdAt").description("created date and time"),
                        fieldWithPath("_embedded.quarBoardLikeRequestDtoList[0].modifiedAt").description("modified date and time"),
                        fieldWithPath("_embedded.quarBoardLikeRequestDtoList[0].quarBoardId").description("the id of the quarBoard post"),
                        fieldWithPath("_embedded.quarBoardLikeRequestDtoList[0].likeCount").description("number of likeCount on the quarBoard"),
                        fieldWithPath("_embedded.quarBoardLikeRequestDtoList[0].title").description("the title of the quarBoard")
                )

                ))
        ;
    }

    @Test
    @TestDescription("유저가 좋아요 누른 medical 가져오기")
    public void 유저가_좋아요_누른_medical_list_가져오기() throws Exception {

        medicalpressLike(5L);
        medicalpressLike(10L);
        medicalpressLike(19L);
        medicalpressLike(20L);
        medicalpressLike(50L);

        this.mockMvc.perform(get("/api/medical/like")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.medicalLikeRequestDtoList[0].medicalId").exists())
                .andExpect(jsonPath("_embedded.medicalLikeRequestDtoList[0].contents").exists())
                .andExpect(jsonPath("_embedded.medicalLikeRequestDtoList[0].likeCount").exists())
                .andExpect(jsonPath("_embedded.medicalLikeRequestDtoList[0].createdAt").exists())
                .andExpect(jsonPath("_embedded.medicalLikeRequestDtoList[0].modifiedAt").exists())

                .andDo(document("mypage_medical_like_list",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.medicalLikeRequestDtoList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.medicalLikeRequestDtoList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.medicalLikeRequestDtoList[0].medicalId").description("the id of the medical post"),
                                fieldWithPath("_embedded.medicalLikeRequestDtoList[0].likeCount").description("number of likeCount on the medical"),
                                fieldWithPath("_embedded.medicalLikeRequestDtoList[0].contents").description("the content of the medical post")
                        )

                ))
        ;
    }
}
