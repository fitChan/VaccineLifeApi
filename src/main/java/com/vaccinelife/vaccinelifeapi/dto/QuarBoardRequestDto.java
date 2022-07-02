package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuarBoardRequestDto {
    private Long id;
    private String title;
    private String contents;
    private int totalVisitors;
    private int likeCount;
    private int commentCount;
    private String username;
    private String nickname;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul" )
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime modifiedAt;

    public static QuarBoardRequestDto of(QuarBoard quarBoard){
        return QuarBoardRequestDto.builder()
                .id(quarBoard.getId())
                .title(quarBoard.getTitle())
                .contents(quarBoard.getContents())
                .totalVisitors(quarBoard.getTotalVisitors())
                .likeCount(quarBoard.getLikeCount())
                .username(quarBoard.getUser().getUsername())
                .nickname(quarBoard.getUser().getNickname())
                .createdAt(quarBoard.getCreatedAt())
                .modifiedAt(quarBoard.getModifiedAt())
                .build();
    }
}
