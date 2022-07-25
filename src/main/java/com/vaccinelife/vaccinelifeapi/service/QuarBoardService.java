package com.vaccinelife.vaccinelifeapi.service;

import com.vaccinelife.vaccinelifeapi.config.Resource.QuarBoardResource;
import com.vaccinelife.vaccinelifeapi.controller.QuarBoardController;
import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.ip.QuarBoardIp;
import com.vaccinelife.vaccinelifeapi.repository.QuarBoardRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.ip.QuarBoardIpRepository;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@AllArgsConstructor
@Service
@Slf4j
public class QuarBoardService {

    private final QuarBoardRepository quarBoardRepository;
    private final UserRepository userRepository;
    private final QuarBoardIpRepository quarBoardIpRepo;
    private final UserService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    //    상세 조회
    @Transactional
    public QuarBoardRequestDto getDetailQuarBoard(Long quarBoardId) {
        QuarBoard quarBoard = quarBoardRepository.findById(quarBoardId).orElseThrow(
                () -> new IllegalArgumentException("userError")
        );

        return QuarBoardRequestDto.of(quarBoard);
    }

    //이전글 다음글
//    @Transactional
//    public QuarPrevNextDto getQuarNextPrevId(Long quarBoardId) {
//        QuarBoard prevId = quarBoardRepository.findTopByIdLessThanOrderByCreatedAtDesc(quarBoardId);
//        QuarBoard nextId = quarBoardRepository.findFirstByIdGreaterThan(quarBoardId);
//        return QuarPrevNextDto.builder()
//                .prevId(prevId)
//                .nextId(nextId)
//                .build();
//    }
//
//    //    전체 조회
//    @Transactional
//    public List<QuarBoardSimRequestDto> getSimpleQuarBoard() {
//        List<QuarBoard> quarBoards = quarBoardRepository.findAllByOrderByCreatedAtDesc();
//        return QuarBoardSimRequestDto.list(quarBoards);
//    }


    //    탑 3
    @Transactional
    public List<QuarBoardTopRequestDto> getTopList() {
        LocalDateTime week = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(0, 0, 0));
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        List<QuarBoard> quarBoards = quarBoardRepository.findTop3ByCreatedAtBetweenOrderByLikeCountDescCreatedAtDesc(week, now);
        return QuarBoardTopRequestDto.list(quarBoards);
    }

    //    게시물 작성
    @Transactional
    public ResponseEntity<QuarBoardResource> createQuarBoard(QuarBoardPostRequestDto requestDto, Long userId) {


        User user = userRepository.getById(userId);
        QuarBoard quarBoard = QuarBoard.builder()
                .user(user)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents()).build();
        QuarBoard save = quarBoardRepository.save(quarBoard);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(QuarBoardController.class).slash(save.getId());
        URI createdUri
                = selfLinkBuilder.toUri();

        QuarBoardResource quarBoardResource = new QuarBoardResource(quarBoard);
        quarBoardResource.add(linkTo(QuarBoardController.class).withRel("query-quarBoards"));
        quarBoardResource.add(Link.of("/docs/index.html#resources-quarBoard-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(quarBoardResource);

    }

    //     게시물 수정
    @Transactional
    public QuarBoard update(Long quarBoardId, QuarBoardRequestDto requestDto, Long userId) throws AccessDeniedException {
        QuarBoard quarBoard = quarBoardRepository.findById(quarBoardId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        if(quarBoard.getUser().getId().equals(userId)) {
            quarBoard.update(requestDto);
            return quarBoard;
        }else{
            throw new AccessDeniedException("작성자만 수정이 가능합니다.");
        }
    }

    //    게시물 삭제
    @Transactional
    public void deleteQuarBoard(Long quarBoardId) {
        QuarBoard quarBoard = quarBoardRepository.findById(quarBoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디값을 찾을 수 없습니다.")
        );
        quarBoardRepository.deleteById(quarBoardId);
    }

    //    ip로 조회수 체크
    @Transactional
    public QuarBoardIp QuarIpChecker(Long id) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String visitorIp = req.getHeader("X-Forwarded-For");

        if (visitorIp == null) {
            visitorIp = req.getHeader("Proxy-Client-IP");
        }

        if (visitorIp == null) {
            visitorIp = req.getHeader("WL-Proxy-Client-IP");
        }

        if (visitorIp == null) {
            visitorIp = req.getHeader("HTTP_CLIENT_IP");
        }
        if (visitorIp == null) {
            visitorIp = req.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (visitorIp == null) {
            visitorIp = req.getRemoteAddr();
        }


        QuarBoard quarBoard = quarBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물 오류")
        );
        QuarBoardIp ip = new QuarBoardIp(visitorIp, quarBoard);

//        List<Ip> IpList = quarBoardIpRepo.findAll();
        boolean isExist = quarBoardIpRepo.existsByQuarBoardAndIp(quarBoard, visitorIp);
        if (!isExist) {
            quarBoardIpRepo.save(ip);
            quarBoard.updateHits(+1);
        } else {
            quarBoard.updateHits(+0);
        }
        return ip;
    }

    //    페이지 처리
//    public Page<QuarBoardSimRequestDto> readQuarBoard(int page, int size, String sortBy, boolean isAsc) {
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        return quarBoardRepository.findAllByOrderByCreatedAtDesc(pageable);
//    }

    //마이페이지 내가 쓴글
    @Transactional
    public List<QuarBoardSimRequestDto> getMypageQuarBoard(Long userId) {
        List<QuarBoard> quarBoards = quarBoardRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        return QuarBoardSimRequestDto.list(quarBoards);
    }
}