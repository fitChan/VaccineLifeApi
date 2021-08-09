package com.vaccinelife.vaccinelifeapi.dto;

import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.MedicalLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class MedicalLikeRequestDto {
    private Long medicalId;
    private Long userId;

    public MedicalLikeRequestDto(Long medicalId, Long userId) {
        this.medicalId = medicalId;
        this.userId = userId;
    }

    public static MedicalLikeRequestDto of(MedicalLike medicalLike) {
        return MedicalLikeRequestDto.builder()
                .userId(medicalLike.getUser().getId())
                .medicalId(medicalLike.getId())
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