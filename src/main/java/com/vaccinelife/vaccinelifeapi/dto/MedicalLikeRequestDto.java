package com.vaccinelife.vaccinelifeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.MedicalLike;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class MedicalLikeRequestDto {
    private Long medicalId;
    private String contents;
    private int likeCount;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul")
    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "asia/seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime modifiedAt;

    @Builder
    public MedicalLikeRequestDto(Long medicalId, String contents, int likeCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.medicalId = medicalId;
        this.contents = contents;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static MedicalLikeRequestDto of(MedicalLike medicalLike) {
        return MedicalLikeRequestDto.builder()
                .medicalId(medicalLike.getMedical().getId())
                .contents(medicalLike.getMedical().getContents())
                .likeCount(medicalLike.getMedical().getLikeCount())
                .createdAt(medicalLike.getMedical().getCreatedAt())
                .modifiedAt(medicalLike.getMedical().getModifiedAt())
                .build();
    }

    public static List<MedicalLikeRequestDto> list(List<MedicalLike> medicalLikes) {
        List<MedicalLikeRequestDto> medicalRequestDtos = new ArrayList<>();
        for (MedicalLike medicalLike : medicalLikes) {
            medicalRequestDtos.add(of(medicalLike));
        }
        return medicalRequestDtos;
    }

}
