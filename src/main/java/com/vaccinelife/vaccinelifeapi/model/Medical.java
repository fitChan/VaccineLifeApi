package com.vaccinelife.vaccinelifeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaccinelife.vaccinelifeapi.dto.MedicalRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor
@Setter @Builder
public class Medical extends Timestamped {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    private Long id;

    @Column(nullable = false)
    private String contents;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private int likeCount;

    @OneToMany(mappedBy = "medical", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<MedicalLike> medicalLikes = new HashSet<>();
    public void updateMedicalLikeNum(int count) {
        this.likeCount += count;
    }

    public Medical(MedicalRequestDto requestDto){
        this.contents = requestDto.getContents();
    }

    public void update(String contents){
        this.contents = contents;
    }

}
