package com.vaccinelife.vaccinelifeapi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaccinelife.vaccinelifeapi.dto.SignupRequestDto;
import com.vaccinelife.vaccinelifeapi.model.enums.AfterEffect;
import com.vaccinelife.vaccinelifeapi.model.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Table(uniqueConstraints=
@UniqueConstraint(columnNames = {"username", "nickname"}))
public class User extends Timestamped{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username" ,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Boolean isVaccine;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Column(nullable = true)
    private Integer degree;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String age;

    @Column(nullable = true)
    private String disease;

    @Column(nullable = true)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<AfterEffect> afterEffect;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"comment"})
    private Set<Comment> comment = new HashSet<>();



    public void add(Comment comment) {
        comment.setUser(this);
        this.comment.add(comment);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"quarcomment"})
    private Set<QuarComment> quarComment = new HashSet<>();
    public void add(QuarComment quarComment) {
        quarComment.setUser(this);
        this.quarComment.add(quarComment);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"vacBoard"})
    private Set<VacBoard> vacBoard = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<VacBoardLike> vacBoardLike = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"quarBoard"})
    private Set<QuarBoard> quarBoard = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<QuarBoardLike> quarBoardLike = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"medical"})
    private Set<Medical> medicals = new HashSet<>();

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<UserRole> role;




    public User(Long id, String username, String password, Set<UserRole> role, String nickname, Boolean isVaccine, Type type,Integer degree, String gender, String age, String disease, Set<AfterEffect> afterEffect) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.isVaccine=isVaccine;
        this.type=type;
        this.degree=degree;
        this.gender=gender;
        this.age= age;
        this.disease= disease;
        this.afterEffect=afterEffect;
    }


    public void update(SignupRequestDto requestDto) {
        this.id=requestDto.getId();
        this.username=requestDto.getUsername();
        this.nickname=requestDto.getNickname();
        this.isVaccine=requestDto.getIsVaccine();
        this.type=requestDto.getType();
        this.degree=requestDto.getDegree();
        this.gender=requestDto.getGender();
        this.age=requestDto.getAge();
        this.disease= requestDto.getDisease();
        this.afterEffect= requestDto.getAfterEffect();

    }
}