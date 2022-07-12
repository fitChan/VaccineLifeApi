package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.dto.MedicalResponseDto;
import com.vaccinelife.vaccinelifeapi.dto.QuarBoardSimRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardSimRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.service.MedicalService;
import com.vaccinelife.vaccinelifeapi.service.QuarBoardService;
import com.vaccinelife.vaccinelifeapi.service.VacBoardService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@AllArgsConstructor
@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    private final VacBoardService vacBoardService;
    private final QuarBoardService quarBoardService;
    private final MedicalService medicalService;
    private final JwtTokenProvider jwtTokenProvider;

    //백신 후기 List 받기
    @GetMapping("/vacBoard") //self Link 추가
    public ResponseEntity<CollectionModel<EntityModel<VacBoardSimRequestDto>>> getMypageVacBoardsample() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        //contextHolder 해서 가져오면 될것 같다. annotation 은 나중에 하는거로
        Long userId = Long.valueOf(principal.toString());
        List<EntityModel<VacBoardSimRequestDto>> result = new ArrayList<>();
        List<VacBoardSimRequestDto> mypageVacBoard = vacBoardService.getMypageVacBoard(userId);

        for(VacBoardSimRequestDto dto : mypageVacBoard){
            EntityModel<VacBoardSimRequestDto> entityModel = EntityModel.of(dto);
            entityModel.add(linkTo(VacBoardController.class).slash(dto.getId()).withRel("vacBoardLink"));
            entityModel.add(Link.of("/docs/index.html#resources-mypage-vacBoard").withRel("profile"));

            result.add(entityModel);
        }
        return ResponseEntity.ok(CollectionModel.of(result));
    }

    @GetMapping("/quarBoard")
    public ResponseEntity<CollectionModel<EntityModel<QuarBoardSimRequestDto>>> getMypageQuarBoard() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Long userId = Long.valueOf(principal.toString());

        List<EntityModel<QuarBoardSimRequestDto>> result = new ArrayList<>();
        List<QuarBoardSimRequestDto> mypageQuar = quarBoardService.getMypageQuarBoard(userId);
        for(QuarBoardSimRequestDto dto : mypageQuar){
            EntityModel<QuarBoardSimRequestDto> entityModel = EntityModel.of(dto);
            entityModel.add(linkTo(QuarBoardController.class).slash(dto.getQuarBoardId()).withRel("quarBoardLink"));
            entityModel.add(Link.of("/docs/index.html#resources-mypage-quarBoard").withRel("profile"));

            result.add(entityModel);
        }

        return ResponseEntity.ok(CollectionModel.of(result));
    }


    //의료진 분들께 한마디 List 받기
    @GetMapping("/medical")
    public ResponseEntity<CollectionModel<EntityModel<MedicalResponseDto>>> getMypageMedical(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Long userId = Long.valueOf(principal.toString());

        List<EntityModel<MedicalResponseDto>> result = new ArrayList<>();

        List<MedicalResponseDto> mypageMedical = medicalService.getMypageMedical(userId);
        for(MedicalResponseDto dto : mypageMedical){
            EntityModel<MedicalResponseDto> entityModel = EntityModel.of(dto);
            entityModel.add(linkTo(MedicalController.class).slash(dto.getId()).withRel("medicalLink"));
            entityModel.add(Link.of("/docs/index.html#resources-mypage-medical").withRel("profile"));
            result.add(entityModel);
        }

        return ResponseEntity.ok(CollectionModel.of(result));
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
