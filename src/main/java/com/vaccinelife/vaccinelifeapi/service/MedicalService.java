package com.vaccinelife.vaccinelifeapi.service;

import javax.transaction.Transactional;

import com.vaccinelife.vaccinelifeapi.dto.*;
import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.repository.MedicalRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
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
    private final UserDetailsServiceImpl userDetailsService;
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
    public List<MedicalResponseDto> getMypageMedical(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getUserPk(token));
        String username =  userDetails.getUsername();

        Optional<User> user = userRepository.findByUsername(username);
        Long userId = user.get().getId();

        List<Medical> medicals = medicalRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        return MedicalResponseDto.list(medicals);
    }


    //    의료진 한마디 작성 기능
    @Transactional
    public void createMedical(MedicalRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );
        Medical medical = new Medical(requestDto);
        medical.setUser(user);
        medicalRepository.save(medical);
    }

    //    의료진 한마디 삭제 기능
    @Transactional
    public void deleteMedical(Long medicalId) {
        Medical medical = medicalRepository.findById(medicalId).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디값을 찾을 수 없습니다.")
        );
        medicalRepository.delete(medical);
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
    public ResponseEntity<Medical> patch(Long medicalId, Map<Object, Object> fields) {
        Optional<Medical> medical = medicalRepository.findById(medicalId);
        if (medical.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Medical.class, (String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, medical.get(), value);
            });
            Medical updateVacBoard = medical.get();
            return new ResponseEntity<>(updateVacBoard, HttpStatus.OK);
        }
        return null;
    }

    public Page<MedicalResponseDto> readMedical(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return medicalRepository.findAllByOrderByCreatedAtDesc(pageable);
    }


}
