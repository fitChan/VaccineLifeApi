package com.vaccinelife.vaccinelifeapi.dto;

import com.vaccinelife.vaccinelifeapi.model.SideEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.SideEffectname;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
@Getter
public class SignupRequestDto {

    @NotBlank(message = "ID는 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "PW는 필수 입력 값입니다.")
    private String password;
    private String passwordChecker;
    private boolean admin;
    private String adminToken;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    private Boolean isVaccine;

    private Type type;

    private int degree;

    private String gender;

    private String age;

    private String disease;

    private List<SideEffectname> afterEffect;


    public SignupRequestDto( String username, String password, String passwordCheck, String nickname,Boolean isVaccine, Type type, int degree, String gender, String age, String disease, SideEffect sideEffect){


    }

    public SignupRequestDto(SignupRequestDto signupRequestDto) {
        this.username=signupRequestDto.getUsername();
        this.nickname=signupRequestDto.getNickname();
        this.isVaccine=signupRequestDto.getIsVaccine();
        this.type= signupRequestDto.getType();
        this.degree=signupRequestDto.getDegree();
        this.gender=signupRequestDto.getGender();
        this.age=signupRequestDto.getAge();
        this.disease=signupRequestDto.getDisease();
        this.afterEffect=signupRequestDto.getAfterEffect();
    }


}