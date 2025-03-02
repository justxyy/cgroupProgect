package cn.gsq.cgroups.engine;

import cn.gsq.cgroups.entity.CgroupParams;
import cn.gsq.cgroups.entity.ResourcePlanEntity;

import java.util.List;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.rpc.CgroupEngine
 *
 * @author : xyy
 * @since : 2024-11-05 17:40
 **/
public interface ResourcePlanEngine {

    List<ResourcePlanEntity> loadPlans();

    void createResourcePlan(ResourcePlanEntity plan);

    void modifyResourcePlan(ResourcePlanEntity plan);

    void delResourcePlan(String planId);

}
