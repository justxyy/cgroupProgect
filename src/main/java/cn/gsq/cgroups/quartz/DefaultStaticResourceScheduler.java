package cn.gsq.cgroups.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.quartz.DefaultStaticResourceScheduler
 *
 * @author : xyy
 * @date : 2024-12-09 11:36
 * @note : It's not technology, it's art !
 **/
@Slf4j
public class DefaultStaticResourceScheduler implements StaticResourceScheduler{

    private static Scheduler scheduler;     // 初始化任务调度器

    private static final String GROUP= "DEFAULT_SRS";

    static {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("任务调度器初始化失败：{}", e.getMessage(), e);
        }
    }


    @Override
    public boolean getPlanStatus(ResourcePlan plan) throws SchedulerException {
        return scheduler.checkExists(new JobKey("job_"+plan.getId(),GROUP));
    }

    @Override
    public void startPlan(ResourcePlan plan) throws SchedulerException {
        JobDataMap startMap = new JobDataMap();
        startMap.put("action", "start");
        startMap.put("plan",plan);

        JobDetail jobDetail1 = JobBuilder.newJob(plan.getClass())
                .withIdentity("job_start_"+plan.getId(), GROUP)
                .usingJobData(startMap)
                .build();

        if(!plan.getId().equals("default")){

            Trigger triggerStart = TriggerBuilder.newTrigger()
                    .withIdentity("trigger_start_"+plan.getId(), GROUP)
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule(plan.getStartCronExp()).withMisfireHandlingInstructionDoNothing()
                    ).build();

            scheduler.scheduleJob(jobDetail1,triggerStart);

            JobDataMap endMap = new JobDataMap();
            endMap.put("action", "end");
            endMap.put("plan",plan);

            JobDetail jobDetail2 = JobBuilder.newJob(plan.getClass())
                    .withIdentity("job_end_"+plan.getId(), GROUP)
                    .usingJobData(endMap)
                    .build();
            Trigger triggerEnd = TriggerBuilder.newTrigger()
                    .withIdentity("trigger_end_"+plan.getId(), GROUP)
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule(plan.getEndCronExp()).withMisfireHandlingInstructionDoNothing()
                    ).build();
            scheduler.scheduleJob(jobDetail2,triggerEnd);
        }else {
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger_start_"+plan.getId(), GROUP)
                    .startNow()  // 立即触发
                    .build();

            scheduler.scheduleJob(jobDetail1,trigger);
        }

    }

    @Override
    public void stopPlan(ResourcePlan plan) throws SchedulerException {
        scheduler.unscheduleJob(new TriggerKey("trigger_start_"+plan.getId(),GROUP));
        if(!plan.getId().equals("default")){
            scheduler.unscheduleJob(new TriggerKey("trigger_end_"+plan.getId(),GROUP));
        }
    }
}
