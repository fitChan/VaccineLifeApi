package com.vaccinelife.vaccinelifeapi.service;

import com.vaccinelife.vaccinelifeapi.dto.MedicalRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.MedicalResponseDto;
import com.vaccinelife.vaccinelifeapi.dto.MedicalTop3RequestDto;
import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.MedicalRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MedicalService {

    private final MedicalRepository medicalRepository;
    private final UserRepository userRepository;
    private final UserService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;


    //    의료진 한마디 조회 기능
    @Transactional
    public List<MedicalResponseDto> getMedicalRequestDto() {
        LocalDateTime month = LocalDateTime.of(LocalDate.now().minusDays(30), LocalTime.of(0, 0, 0));
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        List<Medical> medicals = medicalRepository.findAllByCreatedAtBetweenOrderByCreatedAtDesc(month, now);
        return MedicalResponseDto.list(medicals);
    }

    //내가 쓴 의료진 한마디 조회 하기
    @Transactional
    public List<MedicalResponseDto> getMypageMedical(Long userId) {


        List<Medical> medicals = medicalRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        return MedicalResponseDto.list(medicals);
    }


    //    의료진 한마디 작성 기능
    @Transactional
    public ResponseDto createMedical(MedicalRequestDto requestDto, Long userId) {
        User user = userRepository.getById(userId);
        Medical medical = new Medical(requestDto);
        medical.setUser(user);
        medicalRepository.save(medical);
        return new ResponseDto(true, "medical 게시물 저장 완료", 200);
    }

    //    의료진 한마디 삭제 기능
    @Transactional
    public void deleteMedical(Long medicalId, Long userId) throws AccessDeniedException {
        Medical medical = medicalRepository.findById(medicalId).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디값을 찾을 수 없습니다.")
        );

        if (!medical.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("작성자만 삭제가 가능합니다.");
        } else {
            medicalRepository.delete(medical);
        }


    }

    //    탑 3
    @Transactional
    public List<MedicalTop3RequestDto> getTopList() {
        LocalDateTime week = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(0, 0, 0));
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        List<Medical> medicals = medicalRepository.findTop3ByCreatedAtBetweenOrderByLikeCountDescCreatedAtDesc(week, now);
        return MedicalTop3RequestDto.list(medicals);
    }


    //내용 수정
    @Transactional
    public ResponseDto patch(Long medicalId, Map<Object, Object> fields, Long userId) throws AccessDeniedException {
        Optional<Medical> medical = medicalRepository.findById(medicalId);

        if (userId.equals(medical.get().getUser().getId())){
                fields.forEach((key, value) -> {
                    Field field = ReflectionUtils.findField(Medical.class, (String) key);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, medical.get(), value);
                });
                return new ResponseDto(true, "medical 게시물 수정 완료", 200);
        }else{
            throw  new AccessDeniedException("작성자만 게시물을 수정할 수 있습니다.");
        }

    }

//    public Page<MedicalResponseDto> readMedical(int page, int size, String sortBy, boolean isAsc) {
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
//        return medicalRepository.findAllByOrderByCreatedAtDesc(pageable);
//    }


}
