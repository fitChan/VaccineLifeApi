package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.config.Resource.QuarBoardRequestDtoResource;
import com.vaccinelife.vaccinelifeapi.config.Resource.QuarBoardResource;
import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.repository.QuarBoardRepository;
import com.vaccinelife.vaccinelifeapi.service.QuarBoardService;
import com.vaccinelife.vaccinelifeapi.service.QuarCommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@AllArgsConstructor
@RestController
@RequestMapping("/api/quarBoard")
public class QuarBoardController {

    private final QuarBoardService quarBoardService;
    private final QuarCommentService quarCommentService;
    private final QuarBoardRepository quarBoardRepository;


    //    전체 게시판 조회
    @GetMapping("")
    public ResponseEntity<PagedModel<QuarBoardResource>> getSimpleQuarBoard(Pageable pageable, PagedResourcesAssembler<QuarBoard> assembler) {
        Page<QuarBoard> page = this.quarBoardRepository.findAll(pageable);
        var quarBoardResources = assembler.toModel(page, QuarBoardResource::new);
        quarBoardResources.add(new Link("/docs/index.html#resources-QuarBoard-query").withRel("profile"));

        return ResponseEntity.ok(quarBoardResources);
    }

    //    탑 3
    @GetMapping("/topLike")
    public ResponseEntity<List<QuarBoardTopRequestDto>> getTopList() {
        return ResponseEntity.ok().body(quarBoardService.getTopList());
    }

    //이전글 다음글
    @GetMapping("/{quarBoardId}/id")
    public ResponseEntity<QuarPrevNextDto> getQuarNextPrevId(@PathVariable Long quarBoardId) {
        return ResponseEntity.ok().body(quarBoardService.getQuarNextPrevId(quarBoardId));
    }

    //    상세 게시판 조회
    @GetMapping("/{quarBoardId}")
    public ResponseEntity<QuarBoardRequestDtoResource> getDetailVacBoard(@PathVariable Long quarBoardId) {
        quarBoardService.QuarIpChecker(quarBoardId); // 방문자 체크 로직

        QuarBoardRequestDto quarBoardRequestDto = quarBoardService.getDetailQuarBoard(quarBoardId);
        QuarBoardRequestDtoResource requestDtoResource = new QuarBoardRequestDtoResource(quarBoardRequestDto);
        requestDtoResource.add(Link.of("/docs/index.html#resources-get-vacBoard").withRel("profile"));

        return ResponseEntity.ok().body(requestDtoResource);
    }

    //    게시글 작성
    @PostMapping("")
    public ResponseEntity<?> createQuarBoard(@RequestBody @Valid  QuarBoardPostRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        return quarBoardService.createQuarBoard(requestDto, userId);
    }


    //    게시글 수정
    @PutMapping("/{quarBoardId}")
    public ResponseEntity<?> updateVacBoard(@PathVariable Long quarBoardId, @RequestBody QuarBoardRequestDto requestDto) throws AccessDeniedException {
        String s = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Long userId = Long.valueOf(s);
        QuarBoard update = quarBoardService.update(quarBoardId, requestDto, userId);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(QuarBoardController.class).slash(update.getId());
        URI createdUri
                = selfLinkBuilder.toUri();
        QuarBoardResource quarBoardResource = new QuarBoardResource(update);

        quarBoardResource.add(linkTo(QuarBoardController.class).withRel("update-quarBoard"));
        quarBoardResource.add(Link.of("/docs/index.html#resources-quarBoard-update").withRel("profile"));
        return ResponseEntity.created(createdUri).body(quarBoardResource);
    }

    //    게시글 삭제
    @DeleteMapping("/{quarBoardId}")
    public Long deleteQuarBoard(@PathVariable Long quarBoardId) {
        quarBoardService.deleteQuarBoard(quarBoardId);
        return quarBoardId;
    }

    //    댓글 조회
    @GetMapping("/{quarBoardId}/comments")
    public ResponseEntity<CollectionModel<QuarCommentRequestDto>> getComment(@PathVariable Long quarBoardId) {
        List<QuarCommentRequestDto> quarComment = quarCommentService.getQuarComment(quarBoardId);

        CollectionModel<QuarCommentRequestDto> entitymodel = CollectionModel.of(quarComment);

        entitymodel.add(linkTo(this.getClass()).slash(quarBoardId).withSelfRel());
        entitymodel.add(Link.of("/docs/index.html#resources_query_comments_list_in_quarBoard").withRel("profile"));

        return ResponseEntity.ok(entitymodel);
    }

    //페이지 구현
    @GetMapping("/page")
    public Page<QuarBoardSimRequestDto> readQuarBoard(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc

    ) {

        page = page - 1;
        return quarBoardService.readQuarBoard(page, size, sortBy, isAsc);
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
