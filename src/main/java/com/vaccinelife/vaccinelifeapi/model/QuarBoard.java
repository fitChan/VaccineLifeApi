package com.vaccinelife.vaccinelifeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaccinelife.vaccinelifeapi.dto.QuarBoardRequestDto;
import com.vaccinelife.vaccinelifeapi.model.ip.QuarBoardIp;
import com.vaccinelife.vaccinelifeapi.model.ip.VacBoardIp;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class QuarBoard extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int commentCount;

    @Column(nullable = false)
    private int totalVisitors;


    @JoinColumn(name = "userId")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "quarBoard", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"quarBoard"})
    private Set<QuarComment> quarcomment;
    public void add(QuarComment quarcomment) {
        quarcomment.setQuarBoard(this);
        this.quarcomment.add(quarcomment);
    }


    @Builder
    public QuarBoard(String title, String contents, User user){
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    public void update(QuarBoardRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    @OneToMany(mappedBy = "quarBoard", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<QuarBoardLike> quarBoardLikeList = new HashSet<>();
    public void updateQuarLikeNum(int count) {
        this.likeCount += count;
    }

    @OneToMany(mappedBy = "quarBoard", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<QuarBoardIp> ip = new HashSet<>();
    public void updateHits(int count){
        this.totalVisitors += count;

}
}