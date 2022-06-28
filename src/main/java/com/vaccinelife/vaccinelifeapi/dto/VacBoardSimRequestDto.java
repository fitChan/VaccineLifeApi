package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VacBoardSimRequestDto {
    private Long id;
    private String title;
    private String nickname;
    private int likeCount;
    private int totalVisitors;
    private int commentCount;
    private String type;
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
    @Builder
    public VacBoardSimRequestDto(Long id, String title, String nickname,int likeCount, int totalVisitors, int commentCount, String type, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.likeCount = likeCount;
        this.totalVisitors = totalVisitors;
        this.commentCount = commentCount;
        this.type = type;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static VacBoardSimRequestDto of(VacBoard vacBoard) {
        return VacBoardSimRequestDto.builder()
                .id(vacBoard.getId())
                .title(vacBoard.getTitle())
                .nickname(vacBoard.getUser().getNickname())
                .likeCount(vacBoard.getLikeCount())
                .totalVisitors(vacBoard.getTotalVisitors())
                .commentCount(vacBoard.getCommentCount())
                .type(vacBoard.getUser().getType())
                .createdAt(vacBoard.getCreatedAt())
                .modifiedAt(vacBoard.getModifiedAt())
                .build();
    }

    public static List<VacBoardSimRequestDto> list(List<VacBoard> boards) {
        List<VacBoardSimRequestDto> VacBoardSimRequestDtos = new ArrayList<>();
        for (VacBoard vacBoard : boards) {
            VacBoardSimRequestDtos.add(of(vacBoard));
        }
        return VacBoardSimRequestDtos;
    }


}
