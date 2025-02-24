package cn.gsq.cgroups;

import cn.gsq.cgroups.entity.ResourcePlanEntity;
import cn.gsq.cgroups.quartz.ResourcePlan;
import cn.gsq.cgroups.quartz.StaticResourceScheduler;
import cn.gsq.cgroups.engine.ResourcePlanEngine;
import cn.gsq.common.config.CommonAsyncProcessor;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.oshi.OshiUtil;
import com.sugon.galaxy.common.util.ShellExecutor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import oshi.hardware.GlobalMemory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.CgroupManagerImpl
 *
 * @author : xyy
 * @date : 2024-11-05 13:50
 * @note : It's not technology, it's art !
 **/
@Slf4j
public class CgroupManagerImpl implements CgroupsManager{

    @Autowired
    private CommonAsyncProcessor processor;

    @Autowired
    ResourcePlanEngine engine;//agent端的数据库引擎

    @Autowired
    StaticResourceScheduler scheduler;

    Map<String,ResourcePlan> planMap= new HashMap<>();//内存维护资源配置计划

    public void init(){
        List<ResourcePlanEntity> plans=engine.loadPlans();
        if(ObjectUtil.isNotEmpty(plans)){;
            plans.forEach(e->{

//                System.out.println("------------->");
//                System.out.println(JSONUtil.toJsonStr(e));

                ResourcePlan resourcePlan = new ResourcePlan();
                BeanUtil.copyProperties(e,resourcePlan);
                try {
                    if(resourcePlan.isEnabled()){
                        scheduler.startPlan(resourcePlan);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
                planMap.put(resourcePlan.getId(), resourcePlan);
            });
        }
    }

    @Override
    public void createResourcePlan(ResourcePlanEntity plan) {
        ResourcePlan resourcePlan;
        try {
            //1、agent数据库增加记录
            engine.createResourcePlan(plan);
            //2、启动schedule
            resourcePlan = new ResourcePlan();
            BeanUtil.copyProperties(plan, resourcePlan);
            if(plan.isEnabled()){
                scheduler.startPlan(resourcePlan);
            }
        } catch (Exception e) {
            log.error("createResourcePlan error: " + e.getMessage());
            throw new RuntimeException("createResourcePlan error: " + e.getMessage());
        }
        planMap.put(resourcePlan.getId(), resourcePlan);
    }

    @Override
    public void modifyResourcePlan(ResourcePlanEntity plan) {
        ResourcePlan resourcePlan;
        try {
            //1、agent数据库修改记录
            engine.modifyResourcePlan(plan);
            //2、先停掉在启动schedule
            resourcePlan = new ResourcePlan();
            BeanUtil.copyProperties(plan, resourcePlan);
            scheduler.stopPlan(resourcePlan);

            if(plan.isEnabled()){
                scheduler.startPlan(resourcePlan);
            }else {//恢复default
                ResourcePlan aDefault = planMap.get("default");
                scheduler.stopPlan(aDefault);
                scheduler.startPlan(aDefault);
            }
        } catch (Exception e) {
            log.error("modifyResourcePlan error: " + e.getMessage());
            throw new RuntimeException("modifyResourcePlan error: " + e.getMessage());
        }
        planMap.put(resourcePlan.getId(), resourcePlan);
    }

    @Override
    public void delResourcePlan(String planId) {
        try {
            //1、agent数据库删除记录
            engine.delResourcePlan(planId);
            //2、停掉schedule
            ResourcePlan resourcePlan = planMap.get(planId);
            scheduler.stopPlan(resourcePlan);
            ////恢复default
            ResourcePlan aDefault = planMap.get("default");
            scheduler.stopPlan(aDefault);
            scheduler.startPlan(aDefault);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("delResourcePlan error: "+e.getMessage());
            throw new RuntimeException("delResourcePlan error: "+e.getMessage());
        }
        planMap.remove(planId);
    }

    @Override
    public ResourcePlanEntity getResourcePlanById(String planId) {
        ResourcePlan resourcePlan = planMap.get(planId);
        ResourcePlanEntity resourcePlanEntity = new ResourcePlanEntity();
        BeanUtil.copyProperties(resourcePlan,resourcePlanEntity);
        return resourcePlanEntity;
    }


    @Override
    public void setCgroup(String serveName, double cpuQuota, double cpuShare, double memLimit, boolean interrupt) {
        StringBuilder sb = new StringBuilder();
        if(ObjectUtil.isNotEmpty(cpuQuota)){
            sb.append("quota:"+cpuQuota);
        }
        if(ObjectUtil.isNotEmpty(cpuShare)){
            sb.append(",share:"+cpuShare);
        }
        if(ObjectUtil.isNotEmpty(memLimit)){
            GlobalMemory memory = OshiUtil.getMemory();
            sb.append(",limit:"+(long)(memLimit*memory.getTotal()));
        }
//        if(ObjectUtil.isNotEmpty(interrupt)){
//            sb.append(",interrupt:"+interrupt);
//        }
        String string = sb.toString();
        String substring;
        if(string.startsWith(",")){
            substring = string.substring(1);
        }else {
            substring=string;
        }
        //./cgtool -create cpu:nodemanager_8042 --params quota:50000,share:204
        try {
            int res = ShellExecutor.execute("/usr/galaxy/scripts/cgtool -create " + serveName +" -params "+substring,
                    "",
                    240000L,
                    processor.getService(),
                    (message, p) -> {
                    });
            if (res != 0) {
                log.error("setCgroups 失败");
                throw new RuntimeException("setCgroups 失败");
            }
        } catch (Exception e) {
            log.error("setCgroup 异常："+e.getMessage());
            throw new RuntimeException("setCgroup 异常："+e.getMessage());
        }
    }

    @Override
    public void unsetCgroup(String serveName) {
        try {
            int res = ShellExecutor.execute("/usr/galaxy/scripts/cgtool -delete " + serveName,
                    "",
                    240000L,
                    processor.getService(),
                    (message, p) -> {
                    });
            if (res != 0) {
                log.error("unsetCgroups 失败");
                throw new RuntimeException("unsetCgroups 失败");
            }
        } catch (Exception e) {
            log.error( serveName+" unsetCgroup 异常："+e.getMessage());
            throw new RuntimeException( serveName+" unsetCgroup 异常："+e.getMessage());
        }

    }
}
