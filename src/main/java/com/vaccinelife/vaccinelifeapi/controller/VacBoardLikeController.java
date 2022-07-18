package com.vaccinelife.vaccinelifeapi.controller;

import com.sun.xml.bind.v2.TODO;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.service.VacBoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VacBoardLikeController {

    private final VacBoardLikeService vacBoardLikeService;

    //    좋아요 누르기
    @PostMapping("/api/vacBoard/like")
    public ResponseDto Like(@RequestBody VacBoardLikeRequestDto vacBoardLikeRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        return vacBoardLikeService.Like(vacBoardLikeRequestDto, userId);
    }


    @GetMapping("/api/vacBoard/like")
    public ResponseEntity<List<VacBoardLikeRequestDto>> Like() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        return ResponseEntity.ok().body(vacBoardLikeService.getLike(userId));
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