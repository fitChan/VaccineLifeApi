package com.vaccinelife.vaccinelifeapi.service;

import com.vaccinelife.vaccinelifeapi.dto.QuarBoardLikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.model.*;
import com.vaccinelife.vaccinelifeapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuarBoardLikeService {

    private final QuarBoardLikeRepository quarBoardLikeRepository;
    private final QuarBoardRepository quarBoardRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseDto Like(QuarBoardLikeRequestDto requestDto, Long userId) {
        QuarBoard quarBoard = quarBoardRepository.findById(requestDto.getQuarBoardId()).orElseThrow(
                () -> new NullPointerException("해당 게시물이 존재하지 않습니다.")
        );

        User user = userRepository.getById(userId);

        boolean isExist = quarBoardLikeRepository.existsByQuarBoardAndUser(quarBoard, user);

        if (isExist) {
            quarBoardLikeRepository.deleteByQuarBoardAndUser(quarBoard, user);
            quarBoard.updateQuarLikeNum(-1);
            return new ResponseDto(false, "Basic 게시글 좋아요 취소", 200);
        } else {
            QuarBoardLike quarBoardLike = new QuarBoardLike(quarBoard, user);
            quarBoardLikeRepository.save(quarBoardLike);
            quarBoard.updateQuarLikeNum(+1);
            return new ResponseDto(true, "Basic 게시글 좋아요 추가", 200);
        }
    }

    public List<QuarBoardLikeRequestDto> getLike(Long id) {
        List<QuarBoardLike> quarBoardLike = quarBoardLikeRepository.findAllByUserId(id);
          if(id==null){
            new NullPointerException("아이디가 존재하지 않습니다.");
        }

        return QuarBoardLikeRequestDto.list(quarBoardLike);

    }

}