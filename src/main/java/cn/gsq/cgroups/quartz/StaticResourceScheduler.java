package cn.gsq.cgroups.quartz;

import org.quartz.SchedulerException;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.quartz.StaticResourceScheduler
 *
 * @author : xyy
 * @date : 2024-12-09 11:33
 * @note : It's not technology, it's art !
 **/
public interface StaticResourceScheduler {

    boolean getPlanStatus(ResourcePlan plan) throws SchedulerException;

    void startPlan(ResourcePlan plan) throws SchedulerException;

    void stopPlan(ResourcePlan plan) throws SchedulerException;
}
