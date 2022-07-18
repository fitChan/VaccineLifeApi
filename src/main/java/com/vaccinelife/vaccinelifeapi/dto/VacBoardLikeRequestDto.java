package com.vaccinelife.vaccinelifeapi.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.VacBoardLike;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacBoardLikeRequestDto {
    private Long vacBoardId;
//    private String title;
//    private String contents;
    private int likeCount;
//    private int totalVisitors;
//    private int commentCount;
//    private Type type;
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


    public static VacBoardLikeRequestDto of(VacBoardLike vacBoardLike) {
        return VacBoardLikeRequestDto.builder()
                .vacBoardId(vacBoardLike.getVacBoard().getId())
//                .title(vacBoardLike.getVacBoard().getTitle())
//                .contents(vacBoardLike.getVacBoard().getContents())
                .likeCount(vacBoardLike.getVacBoard().getLikeCount())
//                .totalVisitors(vacBoardLike.getVacBoard().getTotalVisitors())
//                .commentCount(vacBoardLike.getVacBoard().getCommentCount())
//                .type(vacBoardLike.getUser().getType())
                .createdAt(vacBoardLike.getVacBoard().getCreatedAt())
                .modifiedAt(vacBoardLike.getVacBoard().getModifiedAt())
                .build();

    }

    public static List<VacBoardLikeRequestDto> list(List<VacBoardLike> vacBoardLike){
      List<VacBoardLikeRequestDto> vacBoardLikeRequestDtos = new ArrayList<>();
        for(VacBoardLike vacBoardLikes : vacBoardLike){
            vacBoardLikeRequestDtos.add(of(vacBoardLikes));
        }
        return vacBoardLikeRequestDtos;
    }
}