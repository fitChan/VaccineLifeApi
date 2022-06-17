package com.vaccinelife.vaccinelifeapi.model.ip;

import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class VacBoardIp {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    private VacBoard vacBoard;

    public VacBoardIp(String ip, VacBoard vacBoard){
        this.ip = ip;
        this.vacBoard = vacBoard;
    }


}