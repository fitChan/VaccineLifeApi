package com.vaccinelife.vaccinelifeapi.model.enums;

import com.vaccinelife.vaccinelifeapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AfterEffect {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column
    private SideEffectname sideEffectname;

    @JoinColumn(name = "user")
    @ManyToOne
    private User user;


    public AfterEffect(SideEffectname sideEffectname, User user) {
        this.sideEffectname = sideEffectname;
        this.user = user;
    }
}
