package com.vaccinelife.vaccinelifeapi.service;


import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.UserRole;
import com.vaccinelife.vaccinelifeapi.model.enums.AfterEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final BCryptPasswordEncoder passwordEncoder;
    private final  UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";



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
        Long id = requestDto.getId();
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
        Set<AfterEffect> afterEffect = requestDto.getAfterEffect();
        Set<UserRole> role = Collections.singleton(UserRole.USER);

        password = passwordEncoder.encode(password);

        User user = new User(id, username, password, role, nickname, isVaccine, type, degree, gender, age, disease, afterEffect);
        userRepository.save(user);


    }

    @Transactional
    public String findAfterEffect() {
        List<User> none = userRepository.findAllByAfterEffectContaining("없음");
        List<User> fever = userRepository.findAllByAfterEffectContaining("발열");
        List<User> headache = userRepository.findAllByAfterEffectContaining("두통");
        List<User> fatigue = userRepository.findAllByAfterEffectContaining("피로감");
        List<User> pain = userRepository.findAllByAfterEffectContaining("통증");
        List<User> swell = userRepository.findAllByAfterEffectContaining("부기");
        List<User> sickness = userRepository.findAllByAfterEffectContaining("구토");
        List<User> allergy = userRepository.findAllByAfterEffectContaining("알러지");
        List<User> others = userRepository.findAllByAfterEffectContaining("기타");

        int noneNum = none.size();
        int feverNum = fever.size();
        int headacheNum = headache.size();
        int fatigueNum = fatigue.size();
        int painNum = pain.size();
        int swellNum = swell.size();
        int sicknessNum = sickness.size();
        int allergyNum = allergy.size();
        int othersNum = others.size();


        return "{" + "\n" +
                "\"" + "none" + "\"" + ":" + noneNum + "," + "\n" +
                "\"" + "fever" + "\"" + ":" + feverNum + "," + "\n" +
                "\"" + "headache" + "\"" + ":" + headacheNum + "," + "\n" +
                "\"" + "fatigue" + "\"" + ":" + fatigueNum + "," + "\n" +
                "\"" + "pain" + "\"" + ":" + painNum + "," + "\n" +
                "\"" + "swell" + "\"" + ":" + swellNum + "," + "\n" +
                "\"" + "sickness" + "\"" + ":" + sicknessNum + "," + "\n" +
                "\"" + "allergy" + "\"" + ":" + allergyNum + "," + "\n" +
                "\"" + "others" + "\"" + ":" + othersNum + "\n" +
                "}";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<UserRole> role) {
        return role.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE" + r.name()))
                .collect(Collectors.toSet());
    }


}