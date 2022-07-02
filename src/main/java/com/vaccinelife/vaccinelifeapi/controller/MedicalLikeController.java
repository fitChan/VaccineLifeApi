package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.dto.MedicalLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.QuarBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.service.MedicalLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical/like")
@RequiredArgsConstructor
public class MedicalLikeController {
    private final MedicalLikeService medicalLikeService;

    //좋아요 or 좋아요 취소
    @PostMapping("")
    public ResponseDto Like(@RequestBody MedicalLikeRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String s = authentication.getPrincipal().toString();
        Long userId = Long.valueOf(s);
        return medicalLikeService.Like(requestDto, userId);
    }

    //유저별 좋아요 medicalId 받기
    @GetMapping("")
    public ResponseEntity<List<MedicalLikeRequestDto>> getLike() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        return ResponseEntity.ok().body(medicalLikeService.getLike(userId));
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
