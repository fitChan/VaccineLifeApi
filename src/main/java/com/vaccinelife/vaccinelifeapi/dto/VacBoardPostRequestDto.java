package com.vaccinelife.vaccinelifeapi.dto;

import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacBoardPostRequestDto {
    private Long user;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 적어주세요")
    private String contents;

    public VacBoard toEntity(User user){
        return VacBoard.builder()
                .user(user)
                .title(title)
                .contents(contents)
                .build();
    }
}
