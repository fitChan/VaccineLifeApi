package com.vaccinelife.vaccinelifeapi.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;

@Getter
@RestController
@Setter
@Entity
@NoArgsConstructor
public class VacBoardLike {

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