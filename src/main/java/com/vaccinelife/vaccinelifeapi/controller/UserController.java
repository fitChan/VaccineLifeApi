package com.vaccinelife.vaccinelifeapi.controller;

import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;


    //회원가입 Post api
    @PostMapping("/api/signup")
    public void registerUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.registerUser(signupRequestDto);
    }


    //로그인 Post api
    @PostMapping("/api/login")
    public String login(@RequestBody SignupRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));
        if (!userService.bad(requestDto, user)) {
            throw new IllegalArgumentException("비밀번호 확인을 부탁드립니다");
        } else {
            return jwtTokenProvider.createToken(user.getUsername(), user.getId(), user.getRole(), user.getNickname(), user.getIsVaccine(), user.getType(), user.getDegree(), user.getGender(), user.getAge(), user.getDisease(), user.getAfterEffect());
        }
    }

    //user ID 조회로 중복체크 메소드
    @GetMapping("/api/signup/username")
    public ResponseDto UsernameCheck(@RequestParam("username") String username) {

        boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            return new ResponseDto(false, "중복된 ID가 존재합니다", 200);
        } else {
            return new ResponseDto(true, "사용 가능한 ID 입니다.", 200);
        }
    }

    //user nickname 조회로 중복체크 메소드
    @GetMapping("/api/signup/nickname")
    public ResponseDto NicknameCheck(@RequestParam("nickname") String nickname) {

        boolean isExist = userRepository.existsByNickname(nickname);

        if (isExist) {
            return new ResponseDto(false, "중복된 닉네임이 존재합니다", 200);
        } else {
            return new ResponseDto(true, "사용 가능한 닉네임 입니다.", 200);
        }
    }

    //유저 정보 수정 후 새로 바뀐 정보로 토큰 재발급
    @PutMapping("api/signup/{id}")
    public String Userinfo(@PathVariable Long id, @RequestBody SignupRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));
        userService.update(id, requestDto);
        return jwtTokenProvider.createToken(user.getUsername(), user.getId(), user.getRole(), user.getNickname(), user.getIsVaccine(), user.getType(), user.getDegree(), user.getGender(), user.getAge(), user.getDisease(), user.getAfterEffect());
    }

//    @GetMapping("/test")
//    public ResponseEntity<List<TestAfterEffect>> findAfterEffect(){
//        return ResponseEntity.ok().body(userService.findAfterEffect());
//    }

    @GetMapping("/api/main/afterEffect")
    public String findAfterEffect() {
        return userService.findAfterEffect();
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
