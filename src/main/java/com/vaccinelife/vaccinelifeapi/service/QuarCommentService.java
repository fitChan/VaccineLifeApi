package com.vaccinelife.vaccinelifeapi.service;

import com.vaccinelife.vaccinelifeapi.config.Resource.QuarCommentResource;
import com.vaccinelife.vaccinelifeapi.controller.QuarCommentController;
import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.model.*;
import com.vaccinelife.vaccinelifeapi.repository.QuarBoardRepository;
import com.vaccinelife.vaccinelifeapi.repository.QuarCommentRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuarCommentService {

    private final UserRepository userRepository;
    private final QuarBoardRepository quarBoardRepository;
    private final QuarCommentRepository quarCommentRepository;

//    댓글 작성
    @Transactional
    public QuarCommentResource createQuarComment(QuarCommentPostRequestDto requestDto, Long userId) {
        QuarBoard quarBoard = quarBoardRepository.findById(requestDto.getQuarBoardId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
        );
        User user = userRepository.getById(userId);

        List<QuarComment> quarComments = quarCommentRepository.findByQuarBoardId(requestDto.getQuarBoardId());
        int commentSize = quarComments.size();
        quarBoard.setCommentCount(commentSize+1);

        QuarComment quarComment = QuarComment.builder()
                .user(user)
                .quarBoard(quarBoard)
                .quarcomment(requestDto.getQuarcomment()).build();
        quarCommentRepository.save(quarComment);

        QuarCommentResource quarCommentResource = new QuarCommentResource(quarComment);
        quarCommentResource.add(linkTo(QuarCommentController.class).withRel("create-quarBoard-comment"));
        quarCommentResource.add(Link.of("/docs/index.html#resources_comment_create_in_quarBoard").withRel("profile"));
        return quarCommentResource;
    }

//    댓글 삭제
    @Transactional
    public void deleteComment(Long quarCommentId, Long userId) throws AccessDeniedException {
        QuarComment quarComment = quarCommentRepository.findById(quarCommentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.")
        );

        if(quarComment.getUser().getId().equals(userId)) {
            List<QuarComment> quarComments = quarCommentRepository.findByQuarBoardId(quarCommentId);
            int commentSize = quarComments.size();
            Long id = quarComment.getQuarBoard().getId();
            QuarBoard quarBoard = quarBoardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
            );
            quarBoard.setCommentCount(commentSize - 1);
            quarCommentRepository.delete(quarComment);
        }else{
            throw new AccessDeniedException("본인의 댓글만 삭제 가능합니다.");
        }
    }

//    댓글 조회
    @Transactional(readOnly = true)
    public List<QuarCommentRequestDto> getQuarComment(Long quarBoardId) {
        List<QuarComment> quarComments = quarCommentRepository.findAllByQuarBoardIdOrderByCreatedAtDesc(quarBoardId);
        return QuarCommentRequestDto.list(quarComments);
    }

}
