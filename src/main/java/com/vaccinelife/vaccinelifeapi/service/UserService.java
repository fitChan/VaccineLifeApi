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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Component
@Log
public class UserService{


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

    @Transactional
    public void findAfterEffect() {
        /*TODO
        1. STREAM() 쓸꺼고, GROUP BY(?) SUM -> ENUM 으로 들어가서 될거야 , 공부할 것.
        2. 유지 보수 문제가 분명 존재할 것. 문자열 가공은 하지말고 ENUM 으로 TYPE SAFE 하게 할것.
        3. groupBy (or) switch+case
        4. if (sideEffect.get(i).getSideEffectname() == SideEffectname.none) {
        */

        List<SideEffect> sideEffect = sideEffectRepository.findAll();
        int none = 0, fever = 0, headache = 0, fatigue = 0, pain = 0, swell = 0, sickness = 0, allergy = 0, others = 0;
        for (int i = 0; i < sideEffect.size(); i++) {
            if (sideEffect.get(i).getSideEffectname() == SideEffectname.none) {
                none++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("fev")) {
                fever++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("headac")) {
                headache++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("fatig")) {
                fatigue++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("pa")) {
                pain++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("swe")) {
                swell++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("fev")) {
                fever++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("sickne")) {
                sickness++;
            } else if (sideEffect.get(i).getSideEffectname().toString().startsWith("aller")) {
                allergy++;
            } else {
                others++;
            }
        }

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

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new UsernameNotFoundException(username)
//        );
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities(user.getRole()));
//    }
//
//    private Collection<? extends GrantedAuthority> authorities(Set<UserRole> role) {
//        return role.stream()
//                .map(r -> new SimpleGrantedAuthority("ROLE" + r.name()))
//                .collect(Collectors.toSet());
//    }





}