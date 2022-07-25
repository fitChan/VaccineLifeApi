package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.QuarBoardLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuarBoardLikeRequestDto {

    private Long quarBoardId;
    private String title;
//    private String contents;
    private int likeCount;
//    private int commentCount;
//    private int totalVisitors;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul")
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime modifiedAt;





    public static QuarBoardLikeRequestDto of(QuarBoardLike quarBoardLike){
        return QuarBoardLikeRequestDto.builder()
                .quarBoardId(quarBoardLike.getQuarBoard().getId())
                .title(quarBoardLike.getQuarBoard().getTitle())
//                .contents(quarBoardLike.getQuarBoard().getContents())
                .likeCount(quarBoardLike.getQuarBoard().getLikeCount())
//                .commentCount(quarBoardLike.getQuarBoard().getCommentCount())
//                .totalVisitors(quarBoardLike.getQuarBoard().getTotalVisitors())
                .createdAt(quarBoardLike.getQuarBoard().getCreatedAt())
                .modifiedAt(quarBoardLike.getQuarBoard().getModifiedAt())
                .build();
    }
    public static List<QuarBoardLikeRequestDto> list(List<QuarBoardLike> quarBoardLike){
        List<QuarBoardLikeRequestDto> quarLikeRequestDtos = new ArrayList<>();
        for(QuarBoardLike quarBoardLikes : quarBoardLike){
            quarLikeRequestDtos.add(of(quarBoardLikes));
        }
        return quarLikeRequestDtos;
    }
}
