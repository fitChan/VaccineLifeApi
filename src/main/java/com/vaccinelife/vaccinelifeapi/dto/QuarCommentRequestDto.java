package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.QuarComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class QuarCommentRequestDto {
    private Long id;
    private Long quarBoardId;

    private String quarcomment;
    private String nickname;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "asia/seoul")
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime modifiedAt;

    public QuarCommentRequestDto(Long id, Long quarBoardId,  String quarcomment, String nickname, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.quarBoardId = quarBoardId;

        this.quarcomment = quarcomment;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
    public static QuarCommentRequestDto of(QuarComment quarcomment){
        return QuarCommentRequestDto.builder()
                .id(quarcomment.getId())
                .quarBoardId(quarcomment.getQuarBoard().getId())
                .quarcomment(quarcomment.getQuarcomment())
                .nickname(quarcomment.getUser().getNickname())
                .createdAt(quarcomment.getCreatedAt())
                .modifiedAt(quarcomment.getModifiedAt())
                .build();

    }

    public static List<QuarCommentRequestDto> list(List<QuarComment> quarComments){
        List<QuarCommentRequestDto> quarCommentRequestDtos = new ArrayList<>();
        for(QuarComment quarComment : quarComments){
            quarCommentRequestDtos.add(of(quarComment));
        }
        return quarCommentRequestDtos;

    }
}
