package com.laika.IoT.configuration;

import com.laika.IoT.Scheduler.jobs.TimeCheckJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Configuration
public class QuartzConfig {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void start(){
        JobDetail jobDetail = buildJobDetail(TimeCheckJob.class, new HashMap());

        try{
            //job과 트리거 설정
            //20초마다 실행 (확인용)
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/20 * * * * ?"));
            //한시간마다 실행 (진짜 쓸 것)
            //scheduler.scheduleJob(jobDetail, buildJobTrigger("0 0 0 * * ?"));
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    // *  *   *   *   *   *     *
    //초  분  시  일  월  요일  년도(생략가능)
    public Trigger buildJobTrigger(String scheduleExp){
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public JobDetail buildJobDetail(Class job, Map params){
     JobDataMap jobDataMap = new JobDataMap();
     jobDataMap.putAll(params);

     return newJob(job).usingJobData(jobDataMap).build();
    }
}
