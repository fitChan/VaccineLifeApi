package com.vaccinelife.vaccinelifeapi.controller;

import com.sun.xml.bind.v2.TODO;
import com.vaccinelife.vaccinelifeapi.config.Resource.VacBoardResource;
import com.vaccinelife.vaccinelifeapi.config.Resource.VacBoardSimResResource;
import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@AllArgsConstructor
@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    private final VacBoardService vacBoardService;
    private final QuarBoardService quarBoardService;
    private final MedicalService medicalService;
    private final JwtTokenProvider jwtTokenProvider;


    /*@GetMapping("/mypage/vacBoard")
    public ResponseEntity<List<VacBoardSimRequestDto>> getMypageVacBoard(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        jwtTokenProvider.validateToken(token);

        String username = jwtTokenProvider.getUserPk(token);
        List<VacBoardSimRequestDto> mypageVacBoard = vacBoardService.getMypageVacBoard(username);
        return ResponseEntity.ok().body(mypageVacBoard);
    }*/
    //백신 후기 List 받기
    @GetMapping("/vacBoard") //self Link 추가
    public ResponseEntity getMypageVacBoardsample() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        //contextHolder 해서 가져오면 될것 같다. annotation 은 나중에 하는거로
        String username = principal.toString();

        List<VacBoardSimRequestDto> mypageVacBoard = vacBoardService.getMypageVacBoard(username);

        List<VacBoardSimResResource> test = new ArrayList<>();
        for(VacBoardSimRequestDto vacBoardSimRequestDto : mypageVacBoard) {
            VacBoardSimResResource vacBoardSimResResource = new VacBoardSimResResource(vacBoardSimRequestDto);
            WebMvcLinkBuilder self = linkTo(MyPageController.class).slash(vacBoardSimRequestDto.getId());
            URI createdUri = self.toUri();

            vacBoardSimResResource.add(new Link("/docs/index.html#resources-mypage-vacBoard").withRel("profile"));
            test.add(vacBoardSimResResource);

        }
        return ResponseEntity.ok(test);
    }

    @GetMapping("/quarBoard")
    public ResponseEntity<List<QuarBoardSimRequestDto>> getMypageQuarBoard() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = principal.toString();
        return ResponseEntity.ok().body(quarBoardService.getMypageQuarBoard(username));
    }

    //    //의료진 분들께 한마디 List 받기
//    @GetMapping("/{Token}/medical")
//    public ResponseEntity<List<MedicalResponseDto>> getMypageMedical(@PathVariable  String Token){
//        return ResponseEntity.ok().body(medicalService.getMypageMedical(Token));
//    }
    //의료진 분들께 한마디 List 받기
    @GetMapping("/medical")
    public ResponseEntity<List<MedicalResponseDto>> getMypageMedical(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getPrincipal().toString();


        String username = jwtTokenProvider.getUserIdFromJwt(principal);
        return ResponseEntity.ok().body(medicalService.getMypageMedical(username));
    }

    /*TODO
    USER의 정보를 Token 으로 받는것에 있어서 문제가 있을 것 같음.
    그냥 UserCotextHolder에 있는 정보를 그대로 가져다 쓰는건 어떨까? 되는건가?
    토큰을 사용하는 과정인데 이게 되는지 의문.
     */
    @GetMapping("/medical/sample")
    public ResponseEntity<List<MedicalResponseDto>> SampleGetMypageMedical() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();
        return ResponseEntity.ok().body(medicalService.getMypageMedical(username));
    }


    //예외처리 메세지 던질 핸들러
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handle(IllegalArgumentException ex) {
        ApiException apiException = new ApiException(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.BAD_REQUEST
        );
    }
}
