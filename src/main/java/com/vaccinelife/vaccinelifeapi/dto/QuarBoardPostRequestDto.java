package com.vaccinelife.vaccinelifeapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
public class QuarBoardPostRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @NotNull
    private String title;
    @NotBlank(message = "내용을 적어주세요")
    @NotNull
    private String contents;
}
