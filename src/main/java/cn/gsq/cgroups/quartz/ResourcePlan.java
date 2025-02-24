package cn.gsq.cgroups.quartz;

import cn.gsq.cgroups.CgroupsManager;
import cn.gsq.cgroups.entity.ResourceConfig;
import cn.gsq.common.config.GalaxySpringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.quartz.ResourcePlan
 *
 * @author : xyy
 * @since : 2024-12-09 10:55
 **/
@Getter
@Setter
@Accessors(chain = true)
public class ResourcePlan implements Job {

    private String id;

    private String name;

    private String description;

    private boolean enabled;

    private String startCronExp;

    private String endCronExp;

    private String createTime;

    private List<ResourceConfig> resourceConfigs;

    public ResourcePlan() {
    }

    public ResourcePlan(String id, String name, String description, boolean enabled,
                        String startCronExp, String endCronExp, String createTime, List<ResourceConfig> resourceConfigs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.startCronExp = startCronExp;
        this.endCronExp = endCronExp;
        this.createTime = createTime;
        this.resourceConfigs = resourceConfigs;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        ResourcePlan plan = (ResourcePlan) dataMap.get("plan");
        if(plan.isEnabled()){

            String action = dataMap.getString("action"); // "start" 或 "end"
            CgroupsManager cgroupsManager = getBean(CgroupsManager.class);
            if ("start".equals(action)) {
                //  调用cgroup启动逻辑
                plan.getResourceConfigs().forEach(rs->{
                    cgroupsManager.setCgroup(rs.getServeName(), rs.getCpuLimit()*100, 1024, rs.getMemoryLimit(), false);
                });

            } else if ("end".equals(action)) {
                //  调用cgroup停止逻辑
                plan.getResourceConfigs().forEach(rs->{
                    cgroupsManager.unsetCgroup(rs.getServeName());
                    });
            }
        }

    }

    private <T> T getBean(Class<?> clazz) {
        return (T) GalaxySpringUtil.getBean(clazz);
    }
}
