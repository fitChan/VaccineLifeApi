package com.vaccinelife.vaccinelifeapi.repository.ip;

import com.vaccinelife.vaccinelifeapi.model.ip.VacBoardIp;
import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacBoardIpRepository extends JpaRepository<VacBoardIp, Long> {
    boolean existsByVacBoardAndIp(VacBoard vacBoard, String Ip);
}