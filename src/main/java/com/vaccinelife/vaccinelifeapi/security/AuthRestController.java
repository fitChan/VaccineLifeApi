package com.vaccinelife.vaccinelifeapi.security;

import com.vaccinelife.vaccinelifeapi.controller.UserController;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AuthRestController.URL_PREFIX)
public class AuthRestController {

    static final String URL_PREFIX = "/auth";
    static final String LOGIN = "/login";
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


//    public AuthRestController(JwtTokenProvider jwtTokenProvider, UserService userService, UserRepository userRepository) {
//        super(jwtTokenProvider, userService, userRepository);
//    }

    @RequestMapping(
            value = LOGIN,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(
            final HttpServletRequest req,
            final HttpServletResponse res,
            @Valid @RequestBody Token.Request request) throws Exception {

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );


        String encode = passwordEncoder.encode(request.getPassword());
        if (passwordEncoder.matches(encode, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 확인하세요.");
        }

        Authentication authentication = new UserAuthentication( user.getId(), null, null);
        String token = jwtTokenProvider.createToken(String.valueOf(authentication.getPrincipal().toString()));

        Token.Response response = Token.Response.builder().token(token).build();

        return ResponseEntity.ok().body(response);
    }

}
