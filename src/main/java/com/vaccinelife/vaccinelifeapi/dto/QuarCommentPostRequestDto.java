package com.vaccinelife.vaccinelifeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor
@NoArgsConstructor @Builder
public class QuarCommentPostRequestDto {

    private Long quarBoardId;
    private String quarcomment;
}
