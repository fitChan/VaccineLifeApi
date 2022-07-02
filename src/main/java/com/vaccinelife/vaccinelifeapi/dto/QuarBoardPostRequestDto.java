package com.vaccinelife.vaccinelifeapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuarBoardPostRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 적어주세요")
    private String contents;
}
