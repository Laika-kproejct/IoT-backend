package com.laika.IoT.Scheduler.jobs;

import com.laika.IoT.entity.Home;
import com.laika.IoT.provider.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimeCheckJob extends QuartzJobBean {
    private final SensorService sensorService;
    private static final Logger log = LoggerFactory.getLogger(TimeCheckJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("매시간 실행 될 작업 작성 공간");
        LocalDateTime checkDateTime = LocalDateTime.now();
        sensorService.check(checkDateTime);
        System.out.println("체크했습니다!!");
    }
}
