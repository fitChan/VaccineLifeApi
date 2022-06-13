package com.vaccinelife.vaccinelifeapi.ControllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinelife.vaccinelifeapi.common.BaseControllerTest;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardPostRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.TestDescription;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.service.VacBoardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
    VacBoardService vacBoardService;
    @Autowired
    UserRepository userRepository;


    @Test
    @TestDescription("정상적으로 vacBoard를 생성함")
    public void createVacBoard() throws Exception {

        VacBoardPostRequestDto vacBoardPostRequestDto = VacBoardPostRequestDto.builder()
                .user(1L)
                .title("the title")
                .contents("the content")
                .build();

        this.mockMvc.perform(post("/api/vacBoard")
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
                .andDo(document("create_vacBoard",
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
                                fieldWithPath("user").description("userId that is vacBoard information of the PK"),
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
                                fieldWithPath("contents").description("contests of the vacBoard"),
                                fieldWithPath("contents").description("contests of the vacBoard")
//                                fieldWithPath("_links.self.href").description("link to self"),
//                                fieldWithPath("_links.query-vacBoards.href").description("link to query vacBoard")
                        )

                ))
        ;
    }

    @Test
    @TestDescription("입력값이 없을경우 vacBoard 생성 오류")
    public void createVacBoard_Empty_Request() throws Exception {

        VacBoardPostRequestDto vacBoardPostRequestDto = VacBoardPostRequestDto.builder().build();

        this.mockMvc.perform(post("/api/vacBoard")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(vacBoardPostRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("30개의 게시물을 10개씩 페이지 조회하기.")
    public void queryVacBoardList() throws Exception {
        //Given
        IntStream.range(0, 30).forEach(this::generateVacboard);
        //When
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
                .andDo(document("query-vacBoard-List",
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

    private VacBoard generateVacboard(int i) {
        User user = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("없는 유저입니다.")
        );
        VacBoard vacBoard = VacBoard.builder()
                .user(user)
                .title("vacBoard" + i)
                .contents("generated contents for JUnit Test")
                .build();
        return this.vacBoardRepository.save(vacBoard);
    }

    @Test
    @TestDescription("vacBoard 게시물 하나를 조회하는 테스트")
    public void get_vacBoard() throws Exception {
        VacBoard vacBoard = vacBoardRepository.findById(700L).orElseThrow(
                ()-> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        VacBoardRequestDto vacBoardRequestDto = VacBoardRequestDto.builder()
                .id(vacBoard.getId())
                .title(vacBoard.getTitle())
                .contents(vacBoard.getContents())
                .totalVisitors(vacBoard.getTotalVisitors())
                .likeCount(vacBoard.getLikeCount())
                .userId(vacBoard.getUser().getId())
//                .username(vacBoard.getUser().getUsername())
//                .nickname(vacBoard.getUser().getNickname())
//                .isVaccine(vacBoard.getUser().getIsVaccine())
//                .type(vacBoard.getUser().getType())
//                .degree(vacBoard.getUser().getDegree())
//                .gender(vacBoard.getUser().getGender())
//                .age(vacBoard.getUser().getAge())
//                .disease(vacBoard.getUser().getDisease())
//                .afterEffect(vacBoard.getUser().getAfterEffect())
                .createdAt(vacBoard.getCreatedAt())
                .modifiedAt(vacBoard.getModifiedAt())
                .build();
        this.mockMvc.perform(get("/api/vacBoard/{vacBoardId}", vacBoard.getId())
                .content(objectMapper.writeValueAsString(vacBoardRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("contents").exists())
                .andExpect(jsonPath("totalVisitors").exists())
                .andExpect(jsonPath("likeCount").exists())
                .andExpect(jsonPath("userId").exists())
                .andExpect(jsonPath("username").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("isVaccine").exists())
                .andExpect(jsonPath("type").exists())
                .andExpect(jsonPath("degree").exists())
                .andExpect(jsonPath("gender").exists())
                .andExpect(jsonPath("age").exists())
                .andExpect(jsonPath("disease").exists())
                .andExpect(jsonPath("afterEffect").exists())
                .andExpect(jsonPath("degree").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andExpect(jsonPath("_links.self").exists())
        ;
    }


    @Test
    @TestDescription("게시물을 수정하는 테스트")
    public void update_vacBoard() throws Exception {
        User user = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("없는 유저입니다.")
        );
        VacBoard vacBoard = new VacBoard("updated title", "updated contents", user);


        long id = 700L;
        this.mockMvc.perform(put("/api/vacBoard/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(vacBoard)))
                .andDo(print())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("title").value("updated title"))
                .andExpect(jsonPath("contents").value("updated contents"))
                .andExpect(jsonPath("modifiedAt").exists())
                .andDo(document("update-vacBoard",
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
}
