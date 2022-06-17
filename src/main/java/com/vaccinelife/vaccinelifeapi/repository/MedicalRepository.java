package com.vaccinelife.vaccinelifeapi.repository;

import com.vaccinelife.vaccinelifeapi.dto.MedicalRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.MedicalResponseDto;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardSimRequestDto;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalRepository extends JpaRepository<Medical, Long> {

    List<Medical> findAllByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime created, LocalDateTime month);
    List<Medical> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    List<Medical> findTop3ByCreatedAtBetweenOrderByLikeCountDescCreatedAtDesc(LocalDateTime created, LocalDateTime week);
    Page<MedicalResponseDto> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
