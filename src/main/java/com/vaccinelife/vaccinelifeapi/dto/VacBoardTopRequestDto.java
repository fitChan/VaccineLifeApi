package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VacBoardTopRequestDto {
    private Long vacBoardId;
    private String title;
    private String contents;
    private int likeCount;
    private int totalVisitors;
    private int commentCount;
    private Type type;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul" )
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "asia/seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime modifiedAt;


    @Builder
    public VacBoardTopRequestDto(Long vacBoardId, String title, String contents, int likeCount, int totalVisitors, int commentCount, Type type, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.vacBoardId = vacBoardId;
        this.title = title;
        this.contents = contents;
        this.likeCount = likeCount;
        this.totalVisitors = totalVisitors;
        this.commentCount = commentCount;
        this.type = type;
        this.createdAt=createdAt;
        this.modifiedAt=modifiedAt;
    }

    public static VacBoardTopRequestDto of(VacBoard vacBoard){
        return VacBoardTopRequestDto.builder()
                .vacBoardId(vacBoard.getId())
                .title(vacBoard.getTitle())
                .contents(vacBoard.getContents())
                .likeCount(vacBoard.getLikeCount())
                .totalVisitors(vacBoard.getTotalVisitors())
                .createdAt(vacBoard.getCreatedAt())
                .type(vacBoard.getUser().getType())
                .commentCount(vacBoard.getCommentCount())
                .createdAt(vacBoard.getCreatedAt())
                .modifiedAt(vacBoard.getModifiedAt())
                .build();
    }

    public static List<VacBoardTopRequestDto> list(List<VacBoard> boards){
        List<VacBoardTopRequestDto> vacBoardTopRequestDtos = new ArrayList<>();
        for(VacBoard vacBoard : boards){
            vacBoardTopRequestDtos.add(of(vacBoard));
        }
        return vacBoardTopRequestDtos;
    }

}
