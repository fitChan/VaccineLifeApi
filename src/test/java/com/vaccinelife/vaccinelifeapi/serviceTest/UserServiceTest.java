package com.vaccinelife.vaccinelifeapi.serviceTest;


import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.repository.UserRepository;
import com.vaccinelife.vaccinelifeapi.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername() {
        //Given
        String password = "cksdn123";
        String username = "아이디";
        SignupRequestDto user = SignupRequestDto.builder()
                .id(123490871234L)
                .username(username)
                .password(password)
                .passwordChecker(password)
                .nickname("chanoo")
                .isVaccine(true)
                .type("화이자")
                .degree(2)
                .gender("남")
                .age("28")
                .disease("없음")
                .afterEffect("인후통")
                .build();
        this.userService.registerUser(user);
        //When
        UserDetailsService userDetailsService = userService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Test
    public void findByUsernameFail() {
        //Expected ExpectedException 은 먼저 써주는 것.
        String username = "emtyUser@naver.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));
        //When
        userService.loadUserByUsername(username);
    }

}
