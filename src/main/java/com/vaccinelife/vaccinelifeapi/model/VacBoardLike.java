package com.vaccinelife.vaccinelifeapi.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.sql.Time;

@Getter
@RestController
@Setter
@Entity
@NoArgsConstructor
/*TODO CREATEDATE 추가함*/
public class VacBoardLike extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "vacBoardId")
    private VacBoard vacBoard;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;



    public VacBoardLike(VacBoard vacBoard, User user){
        this.vacBoard = vacBoard;
        vacBoard.getVacBoardLikeList().add(this);
        this.user = user;
    }


}