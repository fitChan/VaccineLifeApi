package com.vaccinelife.vaccinelifeapi.controller;


import com.vaccinelife.vaccinelifeapi.config.Resource.QuarCommentResource;
import com.vaccinelife.vaccinelifeapi.dto.QuarCommentPostRequestDto;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.service.QuarCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.file.AccessDeniedException;

@RequestMapping("/api/quarcomment")
@RestController
@RequiredArgsConstructor

public class QuarCommentController {

    private final QuarCommentService quarCommentService;

    //    댓글 작성
    @PostMapping("")
    public ResponseEntity<QuarCommentResource> createQuarComment(@RequestBody QuarCommentPostRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());

        QuarCommentResource quarComment = quarCommentService.createQuarComment(requestDto, userId);
        return ResponseEntity.created(URI.create("/api/quarcomment")).body(quarComment);
    }

    //    댓글 삭제
    @DeleteMapping("/{quarCommentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long quarCommentId) throws AccessDeniedException {
        String s = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Long userId = Long.valueOf(s);

        quarCommentService.deleteComment(quarCommentId, userId);
        return ResponseEntity.ok().build();
    }
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
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handle(AccessDeniedException ex) {
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
