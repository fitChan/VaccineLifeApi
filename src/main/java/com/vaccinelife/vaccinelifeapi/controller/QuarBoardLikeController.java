package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.dto.QuarBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.service.QuarBoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuarBoardLikeController {

    private final QuarBoardLikeService quarBoardLikeService;

//    좋아요 동작
    @PostMapping("/api/quarBoard/like")
    public ResponseDto Like(@RequestBody QuarBoardLikeRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        return quarBoardLikeService.Like(requestDto, userId);
    }

//   유저 기본키로 유저별 좋아요 조회
    @GetMapping("/api/quarBoard/like")
    public ResponseEntity<CollectionModel<QuarBoardLikeRequestDto>> Like() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        List<QuarBoardLikeRequestDto> like = quarBoardLikeService.getLike(userId);
        CollectionModel<QuarBoardLikeRequestDto> entity = CollectionModel.of(like);

        return ResponseEntity.ok().body(entity);
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
