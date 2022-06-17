package com.vaccinelife.vaccinelifeapi.model.ip;

import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class QuarBoardIp {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuarBoard quarBoard;

    public QuarBoardIp(String ip, QuarBoard quarBoard){
        this.ip = ip;
        this.quarBoard = quarBoard;
    }

}