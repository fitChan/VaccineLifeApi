package com.vaccinelife.vaccinelifeapi.dto;

import com.vaccinelife.vaccinelifeapi.model.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class CommentDeleteRequestDto {
    private Long vacBoardId;

    @Builder
    public CommentDeleteRequestDto( Long vacBoardId) {
        this.vacBoardId = vacBoardId;
    }

    public static CommentDeleteRequestDto of(Comment comment){
        return CommentDeleteRequestDto.builder()
                .vacBoardId(comment.getVacBoard().getId())
                .build();

    }
}
