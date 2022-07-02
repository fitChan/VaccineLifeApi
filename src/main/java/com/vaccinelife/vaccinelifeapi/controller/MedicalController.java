package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.repository.MedicalRepository;
import com.vaccinelife.vaccinelifeapi.service.MedicalService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/medical")
public class MedicalController {

    private final MedicalService medicalService;
    private final MedicalRepository medicalRepository;

    //    의료진 한마디 조회
    @GetMapping("")
    public ResponseEntity<List<MedicalResponseDto>> getMedicalRequestDto() {
        return ResponseEntity.ok().body(medicalService.getMedicalRequestDto());
    }

    //TOP3
    @GetMapping("/toplike")
    public ResponseEntity<List<MedicalTop3RequestDto>> getTopList(){
        return ResponseEntity.ok().body(medicalService.getTopList());
    }


    // 의료진 한마디 작성
    @PostMapping("")
    public ResponseEntity<Void> createMedical(@RequestBody MedicalRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());

        medicalService.createMedical(requestDto, userId);
        return ResponseEntity.created(URI.create("/api/medical")).build();
    }


    //    의료진 한마디 삭제
    @DeleteMapping("/{medicalId}")
    public Long deleteMedical(@PathVariable Long medicalId) {
        medicalService.deleteMedical(medicalId);
        return medicalId;
    }

    //의료진 한마디 내용 수정
    @PatchMapping("/{medicalId}")
    public ResponseEntity<Void> patchVacBoard(@PathVariable Long medicalId, @RequestBody Map<Object, Object> fields) {
        medicalService.patch(medicalId, fields);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page")
    public Page<MedicalResponseDto> readMedical(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc

    ) {

        page = page - 1;
        return medicalService.readMedical(page , size, sortBy, isAsc);
    }
    //예외처리 메세지 던질 핸들러
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handle(IllegalArgumentException ex) {
        ApiException apiException = new ApiException(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.BAD_REQUEST
        );
    }


}
