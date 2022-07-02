package com.vaccinelife.vaccinelifeapi.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.Comment;

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
public class CommentRequestDto {
    private  Long id;
    private Long vacBoardId;
    private String comment;
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

    public CommentRequestDto(Long id, Long vacBoardId,  String comment, String nickname, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.vacBoardId = vacBoardId;

        this.comment = comment;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static CommentRequestDto of(Comment comment){
        return CommentRequestDto.builder()
                .id(comment.getId())
                .vacBoardId(comment.getVacBoard().getId())

                .comment(comment.getComment())
                .nickname(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

    }
    public static List<CommentRequestDto> list(List<Comment> comments){
        List<CommentRequestDto> commentRequestDtos = new ArrayList<>();
        for(Comment comment : comments){
            commentRequestDtos.add(of(comment));
        }
        return commentRequestDtos;

    }

}
