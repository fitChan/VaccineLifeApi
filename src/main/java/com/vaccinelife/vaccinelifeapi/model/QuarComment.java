package com.vaccinelife.vaccinelifeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

    @Getter
    @Setter
    @NoArgsConstructor
    @Entity
    public class QuarComment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String quarcomment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "quarBoardId")
    private QuarBoard quarBoard;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
     private User user;


    @Builder
    public QuarComment(String quarcomment, QuarBoard quarBoard, User user) {
     this.quarcomment = quarcomment;
     this.quarBoard = quarBoard;
     this.user = user;
    }

}
