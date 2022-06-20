package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

@AllArgsConstructor
@RestController
@RequestMapping("/api/mypage")
public class MyPageController {

    private final VacBoardService vacBoardService;
    private final QuarBoardService quarBoardService;
    private final MedicalService medicalService;

    //백신 후기 List 받기
//    @GetMapping("/{Token}/vacBoard")
//    public ResponseEntity<List<VacBoardSimRequestDto>> getMypageVacBoard(@PathVariable String Token) {
//        return ResponseEntity.ok().body(vacBoardService.getMypageVacBoard(Token));
//    }
    @GetMapping("/mypage/vacBoard")
    public ResponseEntity<List<VacBoardSimRequestDto>> getMypageVacBoard(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok().body(vacBoardService.getMypageVacBoard(username));
    }

//    //자가 격리 List 받기
//    @GetMapping("/{Token}/quarBoard")
//    public ResponseEntity<List<QuarBoardSimRequestDto>> getMypageQuarBoard(@PathVariable  String Token){
//        return ResponseEntity.ok().body(quarBoardService.getMypageQuarBoard(Token));
//    }

    @GetMapping(value = "/mypage/quarBoard")
    public ResponseEntity<List<QuarBoardSimRequestDto>> getMypageQuarBoard(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok().body(quarBoardService.getMypageQuarBoard(username));
    }

    //    //의료진 분들께 한마디 List 받기
//    @GetMapping("/{Token}/medical")
//    public ResponseEntity<List<MedicalResponseDto>> getMypageMedical(@PathVariable  String Token){
//        return ResponseEntity.ok().body(medicalService.getMypageMedical(Token));
//    }
    //의료진 분들께 한마디 List 받기
    @GetMapping("/mypage/medical")
    public ResponseEntity<List<MedicalResponseDto>> getMypageMedical(Principal principal) {
        String username = principal.getName();
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
