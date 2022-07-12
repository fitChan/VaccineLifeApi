package com.vaccinelife.vaccinelifeapi.service;

import com.vaccinelife.vaccinelifeapi.config.Resource.CommentResource;
import com.vaccinelife.vaccinelifeapi.controller.CommentController;
import com.vaccinelife.vaccinelifeapi.dto.CommentPostRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.CommentRequestDto;
import com.vaccinelife.vaccinelifeapi.model.Comment;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.CommentRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final VacBoardRepository vacBoardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public CommentResource createComment(CommentPostRequestDto requestDto, Long userId) {
        VacBoard vacBoard = vacBoardRepository.findById(requestDto.getVacBoardId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
        );
        User user = userRepository.getById(userId);

        List<Comment> commentCount = commentRepository.findByVacBoardId(requestDto.getVacBoardId());
        int commentSize = commentCount.size();
        vacBoard.setCommentCount(commentSize + 1);
//        Comment comment = modelMapper.map(requestDto, Comment.class);
        Comment comment = Comment.builder()
                .user(user)
                .vacBoard(vacBoard)
                .comment(requestDto.getComment()).build();
        commentRepository.save(comment);


        CommentResource commentResource = new CommentResource(comment);

        commentResource.add(linkTo(CommentController.class).withRel("create-comment"));
        commentResource.add(Link.of("/docs/index.html#resources_comment_create_in_vacBoard").withRel("profile"));
        return commentResource;
    }

    @Transactional(readOnly = true)
    public List<CommentRequestDto> getComment(Long vacBoardId) {
        List<Comment> comment = commentRepository.findAllByVacBoardIdOrderByCreatedAtDesc(vacBoardId);
        return CommentRequestDto.list(comment);
    }

    @Transactional
    public void deleteComment(Long id) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.")
        );
        Long vacBoardId = comment.getVacBoard().getId();
        VacBoard vacBoard = comment.getVacBoard();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getPrincipal().toString();
        Long userId = Long.valueOf(principal);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재 하지 않습니다.")
        );
        Long realId = user.getId();
        Long anonymousId = comment.getUser().getId();
        if (realId.equals(anonymousId)) {
            List<Comment> commentCount = commentRepository.findByVacBoardId(vacBoardId);
            int commentSize = commentCount.size();
            vacBoard.setCommentCount(commentSize - 1);
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
    }

}

