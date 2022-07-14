package com.vaccinelife.vaccinelifeapi.service;


import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.UserRole;
import com.vaccinelife.vaccinelifeapi.model.SideEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.SideEffectname;
import com.vaccinelife.vaccinelifeapi.model.StatisticsAfterEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.repository.SideEffectRepository;
import com.vaccinelife.vaccinelifeapi.repository.StatisticsAfterEffectRepository;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Component
@Log
public class UserService {


    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SideEffectRepository sideEffectRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
    private final StatisticsAfterEffectRepository statisticsAfterEffectRepository;

    public boolean wrongpassword(SignupRequestDto requestDto, String password) {
        if (!passwordEncoder.matches(requestDto.getPassword(), password)) {
            return false;
        }
        return true;
    }

    //유저 정보 업데이트
    @Transactional
    public Long update(Long id, SignupRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );
        user.update(requestDto);
        return id;
    }


    @Transactional
    public void registerUser(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String passwordChecker = requestDto.getPasswordChecker();
        String nickname = requestDto.getNickname();
        Boolean isVaccine = requestDto.getIsVaccine();
        Type type = requestDto.getType();
        int degree = requestDto.getDegree();
        String gender = requestDto.getGender();
        String age = requestDto.getAge();
        String disease = requestDto.getDisease();
        List<SideEffectname> afterEffect = requestDto.getAfterEffect();
        Set<UserRole> role = Collections.singleton(UserRole.USER);

        password = passwordEncoder.encode(password);

        Set<SideEffect> sideEffectList = new HashSet<>();
        User user = new User(username, password, role, nickname, isVaccine, type, degree, gender, age, disease);
        for (SideEffectname e : afterEffect) {
            SideEffect sideEffect1 = new SideEffect(e, user);
            sideEffectRepository.save(sideEffect1);
            sideEffectList.add(sideEffect1);
        }
        user.updateAfterEffect(sideEffectList);
        userRepository.save(user);


    }


    @Scheduled(cron = "0 0 1 * * *")
    public void runEveryHour() {
        findAfterEffect();
        log.info("유저 부작용 업데이트");
    }


    public Map<SideEffectname, List<SideEffect>> sideEffectGroup(List<SideEffect> sideEffects) {
        return sideEffects.stream().collect(Collectors.groupingBy(SideEffect::getSideEffectname));
    }

    public int isthatNull(SideEffectname sideEffectname) {
        List<SideEffect> sideEffect = sideEffectRepository.findAll();
        Map<SideEffectname, List<SideEffect>> collect = sideEffectGroup(sideEffect);
        if (collect.get(sideEffectname) == null) {
            return 0;
        } else {
            return collect.get(sideEffectname).size();
        }
    }

    @Transactional
    public void findAfterEffect() {
        int none = isthatNull(SideEffectname.none);
        int fever = isthatNull(SideEffectname.fever);
        int headache = isthatNull(SideEffectname.headache);
        int fatigue = isthatNull(SideEffectname.fatigue);
        int pain = isthatNull(SideEffectname.pain);
        int swell = isthatNull(SideEffectname.swell);
        int sickness = isthatNull(SideEffectname.sickness);
        int allergy = isthatNull(SideEffectname.allergy);
        int others = isthatNull(SideEffectname.others);

        /*TODO CREATE,UPDATE 같이 되는게 있는 것같다. */
        StatisticsAfterEffect realstatisticsAfterEffect = StatisticsAfterEffect.builder()
                .none(none)
                .fever(fever)
                .fatigue(fatigue)
                .headache(headache)
                .fatigue(fatigue)
                .pain(pain)
                .swell(swell)
                .sickness(sickness)
                .allergy(allergy)
                .others(others)
                .build();
        statisticsAfterEffectRepository.save(realstatisticsAfterEffect);

    }


}