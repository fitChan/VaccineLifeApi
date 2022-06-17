package com.vaccinelife.vaccinelifeapi.service;

import com.vaccinelife.vaccinelifeapi.repository.ip.VacBoardIpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


//ip체크 매일 자정에 리프레쉬 되게
@RequiredArgsConstructor
@Component
@Slf4j
public class Scheduler {

    private final VacBoardIpRepository ipRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초에 실행
    public void initializeTodayVisitors() {
        ipRepository.deleteAll();
    }
}