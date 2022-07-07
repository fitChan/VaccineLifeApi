package com.vaccinelife.vaccinelifeapi.model.enums;

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
public class StatisticsAfterEffect {

    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private int none,fever,headache,fatigue,pain,swell,sickness,allergy,others;
}
