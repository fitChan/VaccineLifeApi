package com.vaccinelife.vaccinelifeapi.controllerTest;


import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardPostRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.model.enums.SideEffectname;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.security.Token;
import com.vaccinelife.vaccinelifeapi.security.UserAuthentication;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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


public class VacBoardControllerTest extends BaseControllerTest {


    @Autowired
    VacBoardRepository vacBoardRepository;
    @Autowired
    UserRepository userRepository;


//    @BeforeAll
//    public static void generateEach(){
//        IntStream.range(0,30).forEach(i->{
//            try {
//                generateEvent(i);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
    /* TODO BeforeEach 문 사용하기 + mock user(user 생성)
     *   2. 모든 API 추가해야함
     *   3. BEFORE-Each 로 DATA 넣어두기 */


    @Test
    @TestDescription("정상적으로 vacBoard를 생성함")
    public void createVacBoard() throws Exception {
        VacBoardPostRequestDto vacBoardPostRequestDto = VacBoardPostRequestDto.builder()

                .title("the title 정상적으로 vacBoard를 생성함")
                .contents("the content 정상적으로 vacBoard를 생성함")
                .build();

        this.mockMvc.perform(post("/api/vacBoard")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(vacBoardPostRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("contents").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-vacBoards").exists())
                .andDo(document("vacBoard-create",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-vacBoards").description("link to query vacBoards"),
                                linkWithRel("profile").description("profile of the [Post]create vacboard")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("title").description("title of the vacBoard"),
                                fieldWithPath("contents").description("contests of the vacBoard")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("id").description("the id of the vacBoard post"),
                                fieldWithPath("title").description("title of the vacBoard"),
                                fieldWithPath("contents").description("content of the vacBoard"),
                                fieldWithPath("totalVisitors").description("number of Visitors of the vacBoard"),
                                fieldWithPath("commentCount").description("number of comments on the vacBoard"),
                                fieldWithPath("likeCount").description("number of likeCount on the vacBoard"),
                                fieldWithPath("contents").description("contests of the vacBoard")
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

    @Test
    @TestDescription("입력값이 없을경우 vacBoard 생성 오류")
    public void createVacBoard_Empty_Request() throws Exception {

        VacBoardPostRequestDto vacBoard = VacBoardPostRequestDto.builder().build();

        this.mockMvc.perform(post("/api/vacBoard")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(vacBoard)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    @TestDescription("30개의 게시물을 10개씩 페이지 조회하기.")
    public void queryVacBoardList() throws Exception {
        this.mockMvc.perform(get("/api/vacBoard")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.vacBoardList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("vacBoard-query-List",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("first").description("link to vacBoard"),
                                linkWithRel("prev").description("link to previous vacBoard page"),
                                linkWithRel("next").description("link to next vacBoard page"),
                                linkWithRel("last").description("link to last vacBoard"),
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
                                fieldWithPath("_embedded.vacBoardList[0].createdAt").description("created date and time"),
                                fieldWithPath("_embedded.vacBoardList[0].modifiedAt").description("modified date and time"),
                                fieldWithPath("_embedded.vacBoardList[0].id").description("the id of the vacBoard post"),
                                fieldWithPath("_embedded.vacBoardList[0].title").description("title of the vacBoard"),
                                fieldWithPath("_embedded.vacBoardList[0].contents").description("content of the vacBoard"),
                                fieldWithPath("_embedded.vacBoardList[0].totalVisitors").description("number of Visitors of the vacBoard"),
                                fieldWithPath("_embedded.vacBoardList[0].commentCount").description("number of comments on the vacBoard"),
                                fieldWithPath("_embedded.vacBoardList[0].likeCount").description("number of likeCount on the vacBoard")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("vacBoard 게시물 하나를 조회하는 테스트")
    public void get_vacBoard() throws Exception {
        VacBoard vacBoard = vacBoardRepository.findById(2L).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        this.mockMvc.perform(get("/api/vacBoard/{vacBoardId}", vacBoard.getId())
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
                .andExpect(jsonPath("isVaccine").exists())
                .andExpect(jsonPath("type").exists())
                .andExpect(jsonPath("degree").exists())
                .andExpect(jsonPath("gender").exists())
                .andExpect(jsonPath("age").exists())
                .andExpect(jsonPath("disease").exists())
                .andExpect(jsonPath("sideEffect").exists())
                .andExpect(jsonPath("degree").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andDo(document("vacBoard-query",
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
                                fieldWithPath("id").description("the id of the vacBoard"),
                                fieldWithPath("title").description("title of the vacBoard"),
                                fieldWithPath("contents").description("content of the vacBoard"),
                                fieldWithPath("totalVisitors").description("number of Visitors of the vacBoard"),
                                fieldWithPath("likeCount").description("number of likeCount on the vacBoard"),
                                fieldWithPath("nickname").description("nickname of the user who posted the vacBoard"),
                                fieldWithPath("isVaccine").description("whether the user who posted the vacBoard has a vaccine"),
                                fieldWithPath("type").description("the vaccine type of the user who posted the vacBoard"),
                                fieldWithPath("degree").description("vaccine rounds"),
                                fieldWithPath("gender").description("the gender of the user who posted the vacBoard"),
                                fieldWithPath("age").description("the age of the user who posted the vacBoard"),
                                fieldWithPath("disease").description("the disease of the user who posted the vacBoard"),
                                fieldWithPath("sideEffect").description("the sideEffect of the user who posted the vacBoard after vaccination")
                        )
                ))
        ;
    }


    @Test
    @TestDescription("게시물을 수정하는 테스트")
    public void update_vacBoard() throws Exception {
        Long id = 3L;
        VacBoard vacBoard = vacBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("없는 게시물입니다.")
        );
        vacBoard.update(VacBoardRequestDto.builder()
                .title("updated title")
                .contents("updated contents")
                .build());


        this.mockMvc.perform(put("/api/vacBoard/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(vacBoard)))
                .andDo(print())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("title").value("updated title"))
                .andExpect(jsonPath("contents").value("updated contents"))
                .andExpect(jsonPath("modifiedAt").exists())
                .andDo(document("vacBoard-update",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("update-vacBoard").description("link to updated vacBoard"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("id").description("id(PK) of the vacBoard"),
                                fieldWithPath("title").description("updated title of the vacBoard"),
                                fieldWithPath("contents").description("updated contents of the vacBoard"),
                                fieldWithPath("modifiedAt").description("modified date and time")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("createdAt").description("created date and time"),
                                fieldWithPath("modifiedAt").description("modified date and time"),
                                fieldWithPath("id").description("id(PK) of the vacBoard"),
                                fieldWithPath("title").description("title of the vacBoard"),
                                fieldWithPath("contents").description("content of the vacBoard"),
                                fieldWithPath("totalVisitors").description("number of Visitors of the vacBoard"),
                                fieldWithPath("commentCount").description("number of comments on the vacBoard"),
                                fieldWithPath("likeCount").description("number of likeCount on the vacBoard")
                        )
                ))
        ;
    }


    @Test
    @TestDescription("타인의 게시물을 수정하는 테스트")
    public void update_vacBoard_access_deny() throws Exception {

        Long id = 2L;
        VacBoard vacBoard = vacBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("없는 게시물입니다.")
        );
        vacBoard.update(VacBoardRequestDto.builder()
                .title("updated title")
                .contents("updated contents")
                .build());


        this.mockMvc.perform(put("/api/vacBoard/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())

        ;
    }

//    public static void generateEvent(int i) throws Exception {
//        User user = userRepository.findById(1L).orElseThrow(
//                () -> new IllegalArgumentException("없는 유저입니다.")
//        );
//        User user2 = userRepository.findById(2L).orElseThrow(
//                () -> new IllegalArgumentException("없는 유저입니다.")
//        );
//
//        if (i % 2 == 0) {
//            VacBoard vacBoard = VacBoard.builder()
//                    .user(user)
//                    .title("user1의 the title" + i + "번째 for Junit Test")
//                    .contents("the content" + i + "번째 for Junit Test")
//                    .build();
//
//            vacBoardRepository.save(vacBoard);
//        } else {
//            VacBoard vacBoard = VacBoard.builder()
//                    .user(user2)
//                    .title("user2의 the title" + i + "번째 for Junit Test")
//                    .contents("the content" + i + "번째 for Junit Test")
//                    .build();
//            vacBoardRepository.save(vacBoard);
//        }
//    }
}
