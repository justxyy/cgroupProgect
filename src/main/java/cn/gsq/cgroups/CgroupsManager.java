package cn.gsq.cgroups;

import cn.gsq.cgroups.entity.CgroupParams;
import cn.gsq.cgroups.entity.ResourcePlanEntity;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.CgroupsManager
 *
 * @author : xyy
 * @since : 2024-11-05 09:54
 **/
public interface CgroupsManager {

    /**
     *  添加资源配置计划
     * @author : xyy
     **/
    void createResourcePlan(ResourcePlanEntity plan);

    /**
     * 修改资源配置计划
     * @author : xyy
     **/
    void modifyResourcePlan(ResourcePlanEntity plan);

    /**
     * 删除资源配置计划
     * @author : xyy
     **/
    void delResourcePlan(String planId);

    /**
     * 根据id获取资源配置计划
     * @author : xyy
     **/
    ResourcePlanEntity getResourcePlanById(String id);


    void setCgroup(String serveName, double cpuQuota,double cpuShare,double memLimit,boolean interrupt);

    void unsetCgroup(String serveName);

}
