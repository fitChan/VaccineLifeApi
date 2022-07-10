package com.vaccinelife.vaccinelifeapi.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsAfterEffect extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int none,fever,headache,fatigue,pain,swell,sickness,allergy,others;

}
