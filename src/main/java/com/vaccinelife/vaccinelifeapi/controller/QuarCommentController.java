package com.vaccinelife.vaccinelifeapi.controller;


import com.vaccinelife.vaccinelifeapi.dto.QuarCommentPostRequestDto;
import com.vaccinelife.vaccinelifeapi.service.QuarCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/quarcomment")
@RestController
@RequiredArgsConstructor

public class QuarCommentController {

    private final QuarCommentService quarCommentService;

//    댓글 작성
   @PostMapping("")
    public ResponseEntity<Void> createQuarComment(@RequestBody QuarCommentPostRequestDto requestDto){
        quarCommentService.createQuarComment(requestDto);
        return ResponseEntity.created(URI.create("/api/quarcomment")).build();
    }

//    댓글 삭제
    @DeleteMapping("/{quarBoardId}/{quarCommentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long quarBoardId, @PathVariable Long quarCommentId) {
        quarCommentService.deleteComment(quarBoardId, quarCommentId);
        return ResponseEntity.ok().build();
    }



}
