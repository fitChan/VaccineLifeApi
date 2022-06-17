package com.vaccinelife.vaccinelifeapi.repository.ip;

import com.vaccinelife.vaccinelifeapi.model.QuarBoard;
import com.vaccinelife.vaccinelifeapi.model.VacBoard;
import com.vaccinelife.vaccinelifeapi.model.ip.QuarBoardIp;
import com.vaccinelife.vaccinelifeapi.model.ip.VacBoardIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuarBoardIpRepository extends JpaRepository<QuarBoardIp, Long> {
    boolean existsByQuarBoardAndIp(QuarBoard quarBoard, String Ip);
}