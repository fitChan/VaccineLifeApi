package com.vaccinelife.vaccinelifeapi.service;

import com.vaccinelife.vaccinelifeapi.config.Resource.VacBoardResource;
import com.vaccinelife.vaccinelifeapi.controller.VacBoardController;
import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.model.ip.VacBoardIp;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import com.vaccinelife.vaccinelifeapi.repository.ip.VacBoardIpRepository;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RequiredArgsConstructor
@Service
@Slf4j
public class VacBoardService {

    private final VacBoardRepository vacBoardRepository;
    private final UserRepository userRepository;
    private final VacBoardIpRepository ipRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userDetailsService;
private final ModelMapper modelMapper;
    //이전글 다음글
    @Transactional

    public VacPrevNextDto getVacNextPrevId(Long vaBoardId) {
        VacBoard prevId = vacBoardRepository.findTopByIdLessThanOrderByCreatedAtDesc(vaBoardId);
        VacBoard nextId = vacBoardRepository.findFirstByIdGreaterThan(vaBoardId);
        return VacPrevNextDto.builder()
                .prevId(prevId)
                .nextId(nextId)
                .build();
    }


    //    상세조회
    @Transactional
    public VacBoardRequestDto getDetailVacBoard(Long vacBoardId) {
        VacBoard vacBoard = vacBoardRepository.findById(vacBoardId).orElseThrow(
                () -> new IllegalArgumentException("userError")
        );
        return VacBoardRequestDto.of(vacBoard);
    }

    //    전체조회
    @Transactional
    public List<VacBoardSimRequestDto> getSimpleVacBoard() {
        List<VacBoard> vacBoards = vacBoardRepository.findAllByOrderByCreatedAtDesc();
        return VacBoardSimRequestDto.list(vacBoards);
    }

    //    탑 3
    @Transactional
    public List<VacBoardTopRequestDto> getTopList() {
        LocalDateTime week = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(0, 0, 0));
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        List<VacBoard> vacBoards = vacBoardRepository.findTop3ByCreatedAtBetweenOrderByLikeCountDescCreatedAtDesc(week, now);
        return VacBoardTopRequestDto.list(vacBoards);
    }

    // 게시물 작성
//    @Transactional
//    public User createVacBoard(VacBoardPostRequestDto requestDto) {
//        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(
//                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
//        );
//        vacBoardRepository.save(requestDto.toEntity(user));
//    }

    // 게시물 수정
    @Transactional
    public VacBoard update(Long vacBoardId, VacBoardRequestDto requestDto) {
        VacBoard vacBoard = vacBoardRepository.findById(vacBoardId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        vacBoard.update(requestDto);
        return vacBoard;
    }

    // 게시물 삭제
    @Transactional
    public void deleteVacBoard(Long vacBoardId) {
        VacBoard vacBoard = vacBoardRepository.findById(vacBoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디값을 찾을 수 없습니다.")
        );
        vacBoardRepository.deleteById(vacBoardId);
    }

    //    ip로 조회수 체크
    @Transactional
    public Object IpChecker(Long id) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String clientIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            //Proxy 서버인 경우
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            //Weblogic 서버인 경우
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        VacBoard vacBoard = vacBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물 오류")
        );
        VacBoardIp ip = new VacBoardIp(clientIp, vacBoard);
//        List<Ip> IpList = ipRepository.findAll();
        boolean isExist = ipRepository.existsByVacBoardAndIp(vacBoard, clientIp);
        if (!isExist) {
            ipRepository.save(ip);
            vacBoard.updateHits(+1);
        } else {
            vacBoard.updateHits(+0);
        }
        return clientIp;
    }

    //    //mypage vacboard
//    @Transactional
//    public List<VacBoardSimRequestDto> getMypageVacBoard(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getUserPk(token));
//        String username = userDetails.getUsername();
//
//        Optional<User> user = userRepository.findByUsername(username);
//        Long userId = user.get().getId();
//        List<VacBoard> vacBoards = vacBoardRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
//        return VacBoardSimRequestDto.list(vacBoards);
//
//    }
    @Transactional
    public List<VacBoardSimRequestDto> getMypageVacBoard(Long userId) {

        List<VacBoard> vacBoards = vacBoardRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        return VacBoardSimRequestDto.list(vacBoards);

    }


    //게시판 무한스크롤
    public Page<VacBoardSimRequestDto> readVacBoard(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return vacBoardRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 백신 타입별 필터링 + 동시에 무한스크롤
    public Page<VacBoardSimRequestDto> readVacBoardType(int page, int size, String sortBy, boolean isAsc, String type) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return vacBoardRepository.findAllByUserTypeOrderByCreatedAtDesc(pageable, type);
    }


    public ResponseEntity<VacBoardResource> createVacBoardUrl(VacBoardPostRequestDto requestDto, User user) {
        VacBoard vacBoard = modelMapper.map(requestDto, VacBoard.class);

        vacBoard.setUser(user);
        VacBoard newVacBoard = this.vacBoardRepository.save(vacBoard);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(VacBoardController.class).slash(newVacBoard.getId());
        URI createdUri
                = selfLinkBuilder.toUri();

        VacBoardResource vacBoardResource = new VacBoardResource(vacBoard);
        vacBoardResource.add(linkTo(VacBoardController.class).withRel("query-vacBoards"));
        vacBoardResource.add(Link.of("/docs/index.html#resources-vacBoard-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(vacBoardResource);
    }
}
