package com.vaccinelife.vaccinelifeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaccinelife.vaccinelifeapi.dto.VacBoardRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.*;


@NoArgsConstructor
@Entity
@Getter
@Setter
public class VacBoard extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int totalVisitors;

    @Column(nullable = false)
    private int commentCount;

    @Column(nullable = false)
    private int likeCount;


    @JoinColumn(name = "userId")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "vacBoard", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"vacBoard"})
    private Set<Comment> comment;

    @Builder
    public VacBoard(String title, String contents, User user) {
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    public void update(VacBoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    @OneToMany(mappedBy = "vacBoard", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<VacBoardLike> vacBoardLikeList = new HashSet<>();
    public void updateLikeNum(int count) {
        this.likeCount += count;
    }

    @OneToMany(mappedBy = "vacBoard", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<Ip> ip = new HashSet<>();
    public void updateHits(int count){
        this.totalVisitors += count;
    }
}