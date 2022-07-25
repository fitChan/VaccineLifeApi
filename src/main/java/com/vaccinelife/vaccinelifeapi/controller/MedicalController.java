package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.repository.MedicalRepository;
import com.vaccinelife.vaccinelifeapi.service.MedicalService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/medical")
public class MedicalController {

    private final MedicalService medicalService;

    //    의료진 한마디 조회
    @GetMapping("")
    public ResponseEntity<CollectionModel<MedicalResponseDto>> getMedicalRequestDto() {
        List<MedicalResponseDto> medicalRequestDto = medicalService.getMedicalRequestDto();

        CollectionModel<MedicalResponseDto> entityModel = CollectionModel.of(medicalRequestDto);

        return ResponseEntity.ok().body(entityModel);
    }

    //TOP3
    @GetMapping("/topLike")
    public ResponseEntity<CollectionModel<MedicalTop3RequestDto>> getTopList(){

        List<MedicalTop3RequestDto> topList = medicalService.getTopList();

        CollectionModel<MedicalTop3RequestDto> entityModel = CollectionModel.of(topList);

        return ResponseEntity.ok().body(entityModel);
    }


    // 의료진 한마디 작성
    @PostMapping("")
    public ResponseEntity<ResponseDto> createMedical(@RequestBody MedicalRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getPrincipal().toString());

        ResponseDto medical = medicalService.createMedical(requestDto, userId);

        return ResponseEntity.created(URI.create("/api/medical")).body(medical);
    }


    //    의료진 한마디 삭제
    @DeleteMapping("/{medicalId}")
    public Long deleteMedical(@PathVariable Long medicalId) throws AccessDeniedException{

        String s = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Long userId = Long.valueOf(s);

        medicalService.deleteMedical(medicalId, userId);
        return medicalId;
    }

    //의료진 한마디 내용 수정
    @PatchMapping("/{medicalId}")
    public ResponseEntity<?> patchVacBoard(@PathVariable Long medicalId, @RequestBody Map<Object, Object> fields) throws AccessDeniedException{
        String s = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Long userId = Long.valueOf(s);
        ResponseDto patch = medicalService.patch(medicalId, fields, userId);
        return ResponseEntity.ok().body(patch);
    }

//    @GetMapping("/page")
//    public Page<MedicalResponseDto> readMedical(
//            @RequestParam("page") int page,
//            @RequestParam("size") int size,
//            @RequestParam("sortBy") String sortBy,
//            @RequestParam("isAsc") boolean isAsc
//
//    ) {
//
//        page = page - 1;
//        return medicalService.readMedical(page , size, sortBy, isAsc);
//    }
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
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handle(AccessDeniedException ex) {
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
