package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.model.SideEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacBoardRequestDto {
    private Long id;
    private String title;
    private String contents;
    private int totalVisitors;
    private int likeCount;
    private String username;
    private String nickname;
    private Boolean isVaccine;
    private Type type;
    private Integer degree;
    private String gender;
    private String age;
    private String disease;
    private Set<SideEffect> sideEffect;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime modifiedAt;


    public static VacBoardRequestDto of(VacBoard vacBoard) {
        return VacBoardRequestDto.builder()
                .id(vacBoard.getId())
                .title(vacBoard.getTitle())
                .contents(vacBoard.getContents())
                .totalVisitors(vacBoard.getTotalVisitors())
                .likeCount(vacBoard.getLikeCount())
                .username(vacBoard.getUser().getUsername())
                .nickname(vacBoard.getUser().getNickname())
                .isVaccine(vacBoard.getUser().getIsVaccine())
                .type(vacBoard.getUser().getType())
                .degree(vacBoard.getUser().getDegree())
                .gender(vacBoard.getUser().getGender())
                .age(vacBoard.getUser().getAge())
                .disease(vacBoard.getUser().getDisease())
                .sideEffect(vacBoard.getUser().getSideEffect())
                .createdAt(vacBoard.getCreatedAt())
                .modifiedAt(vacBoard.getModifiedAt())
                .build();
            }
}