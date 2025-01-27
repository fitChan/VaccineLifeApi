package com.vaccinelife.vaccinelifeapi.controller;


import com.vaccinelife.vaccinelifeapi.config.Resource.VacBoardRequestDtoResource;
import com.vaccinelife.vaccinelifeapi.config.Resource.VacBoardResource;
import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.service.CommentService;
import com.vaccinelife.vaccinelifeapi.service.VacBoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vacBoard")

public class VacBoardController {
    private final VacBoardService vacBoardService;
    private final VacBoardRepository vacBoardRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    //    전체 게시판 조회
    @GetMapping("")
    public ResponseEntity<PagedModel<VacBoardResource>> getSimpleVacBoard(Pageable pageable, PagedResourcesAssembler<VacBoard> assembler) {
        Page<VacBoard> page = this.vacBoardRepository.findAll(pageable);
        var pagedResources = assembler.toModel(page, VacBoardResource::new);
        pagedResources.add(new Link("/docs/index.html#resources-vacBoard-query").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    //    탑 3
    @GetMapping("/topLike")
    public ResponseEntity<CollectionModel<VacBoardTopRequestDto>> getTopList() {
        List<VacBoardTopRequestDto> topList = vacBoardService.getTopList();
        CollectionModel<VacBoardTopRequestDto> entityModel = CollectionModel.of(topList);


        return ResponseEntity.ok().body(entityModel);
    }

    //이전글 다음글
//    @GetMapping("/{vacBoardId}/id")
//    public ResponseEntity<VacPrevNextDto> getNPId(@PathVariable Long vacBoardId) {
//        return ResponseEntity.ok().body(vacBoardService.getVacNextPrevId(vacBoardId));
//    }

    //    상세 게시판 조회
    @GetMapping("/{vacBoardId}")
    public ResponseEntity<VacBoardRequestDtoResource> getDetailVacBoard(@PathVariable Long vacBoardId) {
        vacBoardService.IpChecker(vacBoardId); // 방문자 체크 로직

        VacBoardRequestDto newVacBoardRequestDto = vacBoardService.getDetailVacBoard(vacBoardId);
        VacBoardRequestDtoResource vacBoardRequestDtoResource = new VacBoardRequestDtoResource(newVacBoardRequestDto);
        vacBoardRequestDtoResource.add(Link.of("/docs/index.html#resources-get-vacBoard").withRel("profile"));

        return ResponseEntity.ok().body(vacBoardRequestDtoResource);
    }

    @GetMapping("/{vacBoardId}/comments")
    public ResponseEntity<CollectionModel<CommentRequestDto>> getComment(@PathVariable Long vacBoardId) {
        List<CommentRequestDto> comment = commentService.getComment(vacBoardId);
        CollectionModel<CommentRequestDto> entityModel = CollectionModel.of(comment);

        entityModel.add(linkTo((this.getClass())).slash(vacBoardId).withSelfRel());

        entityModel.add(Link.of("/docs/index.html#resources_query_comments_list_in_vacBoard").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("")
    public ResponseEntity<?> createVacBoard(@RequestBody @Valid VacBoardPostRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        if (authentication.isAuthenticated()) {
            User user = userRepository.getById(userId);
            if (user.getIsVaccine()) {
                return vacBoardService.createVacBoardUrl(requestDto, user);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().body(new IllegalArgumentException("로그인이 되어있지 않습니다."));
        }
    }

    //    게시글 수정
    @PutMapping("/{vacBoardId}")
    public ResponseEntity updateVacBoard(@PathVariable Long vacBoardId, @RequestBody VacBoardRequestDto requestDto) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String s = authentication.getPrincipal().toString();
        Long userId = Long.valueOf(s);
        VacBoard vacBoard = vacBoardService.update(vacBoardId, requestDto, userId);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(VacBoardController.class).slash(vacBoard.getId());
        URI createdUri
                = selfLinkBuilder.toUri();
        VacBoardResource vacBoardResource = new VacBoardResource(vacBoard);

        vacBoardResource.add(linkTo(VacBoardController.class).withRel("update-vacBoard"));
        vacBoardResource.add(new Link("/docs/index.html#resources-vacBoard-update").withRel("profile"));

        return ResponseEntity.created(createdUri).body(vacBoardResource);
    }

    //    게시글 삭제
    @DeleteMapping("/{vacBoardId}")
    public Long deleteVacBoard(@PathVariable Long vacBoardId) {
        vacBoardService.deleteVacBoard(vacBoardId);
        return vacBoardId;
    }

    //페이지 구현
//    @GetMapping("/page")
//    public Page<VacBoardSimRequestDto> readVacBoard(
//            @RequestParam("page") int page,
//            @RequestParam("size") int size,
//            @RequestParam("sortBy") String sortBy,
//            @RequestParam("isAsc") boolean isAsc
//
//    ) {
//
//        page = page - 1;
//        return vacBoardService.readVacBoard(page, size, sortBy, isAsc);
//    }


    //백신 종류별 필터링 + 페이지 구현
//    @GetMapping("type/page")
//    public Page<VacBoardSimRequestDto> readVacBoardType(
//            @RequestParam("page") int page,
//            @RequestParam("size") int size,
//            @RequestParam("sortBy") String sortBy,
//            @RequestParam("isAsc") boolean isAsc,
//            @RequestParam("type") String type
//
//    ) {
//
//        page = page - 1;
//        return vacBoardService.readVacBoardType(page, size, sortBy, isAsc, type);
//    }

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

