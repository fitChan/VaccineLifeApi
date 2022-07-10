package com.vaccinelife.vaccinelifeapi.model;

import com.vaccinelife.vaccinelifeapi.model.User;
import com.vaccinelife.vaccinelifeapi.model.enums.SideEffectname;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SideEffect {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column
    private SideEffectname sideEffectname;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;


    public SideEffect(SideEffectname sideEffectname, User user) {
        this.sideEffectname = sideEffectname;
        this.user = user;
    }
}
