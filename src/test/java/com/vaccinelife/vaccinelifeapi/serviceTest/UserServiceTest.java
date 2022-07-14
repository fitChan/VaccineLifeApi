package com.vaccinelife.vaccinelifeapi.serviceTest;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;



@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//    @Autowired
//    UserService userService;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Test
//    public void findByUsername() {
//        //Given
//        String password = "cksdn123";
//        String username = "아이디";
//        SignupRequestDto user = SignupRequestDto.builder()
//                .username(username)
//                .password(password)
//                .passwordChecker(password)
//                .nickname("chanoo")
//                .isVaccine(true)
//                .type(Type.AZ)
//                .degree(2)
//                .gender("남")
//                .age("28")
//                .disease("없음")
//                .afterEffect(Collections.singletonList(SideEffectname.none))
//                .build();
//        this.userService.registerUser(user);
//        //When
//        UserDetailsService userDetailsService = userService;
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        //Then
//        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
//    }
//
//    @Test
//    public void findByUsernameFail() {
//        //Expected ExpectedException 은 먼저 써주는 것.
//        String username = "emtyUser@naver.com";
//        expectedException.expect(UsernameNotFoundException.class);
//        expectedException.expectMessage(Matchers.containsString(username));
//        //When
//        userService.loadUserByUsername(username);
//    }

}
