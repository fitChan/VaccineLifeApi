package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class MedicalResponseDto {
    private Long id;
    private String contents;
    private String nickname;

    private int likeCount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "asia/seoul")
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    public static MedicalResponseDto of(Medical medical){
        return MedicalResponseDto.builder()
                .id(medical.getId())
                .contents(medical.getContents())
                .nickname(medical.getUser().getNickname())
                .likeCount(medical.getLikeCount())
                .createdAt(medical.getCreatedAt())
                .build();
    }

    public static List<MedicalResponseDto> list(List<Medical> medicals){
        List<MedicalResponseDto> medicalResponseDtos = new ArrayList<>();
        for(Medical medicalList : medicals){
            medicalResponseDtos.add(of(medicalList));
        }
        return medicalResponseDtos;
    }

}
