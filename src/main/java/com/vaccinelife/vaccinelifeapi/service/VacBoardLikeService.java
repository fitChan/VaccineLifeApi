package com.vaccinelife.vaccinelifeapi.service;

import com.sun.xml.bind.v2.TODO;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoardLike;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardLikeRepository;
import com.vaccinelife.vaccinelifeapi.repository.VacBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class VacBoardLikeService {

    private final VacBoardLikeRepository vacBoardLikeRepository;
    private final VacBoardRepository vacBoardRepository;
    private final UserRepository userRepository;


    /*TODO 1. 생각해볼것 -> 동시요청 / 한번에 여러번 눌렀을 경우 어캐 될지 생각 해볼것. */
    @Transactional
    public ResponseDto Like(VacBoardLikeRequestDto vacBoardLikeRequestDto, Long userId) {
        VacBoard vacBoard = vacBoardRepository.findById(vacBoardLikeRequestDto.getVacBoardId()).orElseThrow(
                () -> new NullPointerException("해당 게시물이 존재하지 않습니다.")
        );
        User user = userRepository.getById(userId);

        boolean isExist = vacBoardLikeRepository.existsByVacBoardAndUser(vacBoard, user);

        //좋아요 Post를 이미 한 적이 있으면 Post 시 좋아요 취소 카운트 -1,  없으면 좋아요 추가 카운트 +1
        if (isExist) {
            vacBoardLikeRepository.deleteByVacBoardAndUser(vacBoard, user);
            vacBoard.updateLikeNum(-1);
            return new ResponseDto(false, "vacBoard like is canceled", 200);
        } else {
            VacBoardLike vacBoardLike = new VacBoardLike(vacBoard, user);
            vacBoardLikeRepository.save(vacBoardLike);
            vacBoard.updateLikeNum(+1);
            return new ResponseDto(true, "vacBoard like is applied", 200);
        }
    }

    //userID로 유저별로 좋아요 한 게시글 조회
    public List<VacBoardLikeRequestDto> getLike(Long userId) {
        if (userId == null) {
            throw new NullPointerException("아이디가 존재하지 않습니다.");
        }
        List<VacBoardLike> vacBoardLike = vacBoardLikeRepository.findAllByUserId(userId);

        return VacBoardLikeRequestDto.list(vacBoardLike);
    }


}
