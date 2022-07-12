package com.vaccinelife.vaccinelifeapi.controllerTest;

import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
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
import org.springframework.security.core.Authentication;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MypageControllerTest extends BaseControllerTest {

    @Autowired
    VacBoardRepository vacBoardRepository;
    @Autowired
    VacBoardService vacBoardService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
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
}
