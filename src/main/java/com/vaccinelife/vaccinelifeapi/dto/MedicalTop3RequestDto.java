package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder @AllArgsConstructor @Transactional
public class MedicalTop3RequestDto {
    private Long id;
    private String contents;
    private String nickname;
    private int likeCount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "asia/seoul")
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    public static MedicalTop3RequestDto of(Medical medical){
        return MedicalTop3RequestDto.builder()
                .id(medical.getId())
                .contents(medical.getContents())
                .nickname(medical.getUser().getNickname())
                .likeCount(medical.getLikeCount())
                .createdAt(medical.getCreatedAt())
                .build();
    }

    public static List<MedicalTop3RequestDto> list(List<Medical> medicals){
        List<MedicalTop3RequestDto> medicalTop3RequestDtos = new ArrayList<>();
        for(Medical medical : medicals){
            medicalTop3RequestDtos.add(of(medical));
        }
        return medicalTop3RequestDtos;
    }
}
