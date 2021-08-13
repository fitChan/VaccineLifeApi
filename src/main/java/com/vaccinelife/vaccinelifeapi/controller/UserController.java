package com.vaccinelife.vaccinelifeapi.controller;
import com.vaccinelife.vaccinelifeapi.dto.LikeRequestDto;
import com.vaccinelife.vaccinelifeapi.dto.ResponseDto;
import com.vaccinelife.vaccinelifeapi.exception.ApiException;
import com.vaccinelife.vaccinelifeapi.model.VacBoardLike;
import com.vaccinelife.vaccinelifeapi.security.JwtTokenProvider;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;


    @PostMapping("/api/signup")
    public void registerUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.registerUser(signupRequestDto);
    }

    @PostMapping("/api/login")
    public String login(@RequestBody SignupRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createToken(user.getUsername(), user.getId(), user.getRole(), user.getNickname(), user.getIsVaccine(), user.getType(),user.getDegree(),user.getGender(),user.getAge(),user.getDisease(),user.getAfterEffect());
    }

    @GetMapping("/api/signup/username")
    public ResponseDto UsernameCheck( @RequestParam("username") String username) {

    boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
        return new ResponseDto(false, "중복된 ID가 존재합니다", 200);
    } else {
        return new ResponseDto(true, "사용 가능한 ID 입니다.", 200);
        }
    }

    @GetMapping("/api/signup/nickname")
    public ResponseDto NicknameCheck( @RequestParam("nickname") String nickname) {

        boolean isExist = userRepository.existsByNickname(nickname);

        if (isExist) {
            return new ResponseDto(false, "중복된 닉네임이 존재합니다", 200);
        } else {
            return new ResponseDto(true, "사용 가능한 닉네임 입니다.", 200);
        }
    }


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