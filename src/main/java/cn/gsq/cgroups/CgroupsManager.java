package cn.gsq.cgroups;

import cn.gsq.cgroups.entity.CgroupParams;
import cn.gsq.cgroups.entity.ResourcePlanEntity;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.CgroupsManager
 *
 * @author : xyy
 * @date : 2024-11-05 09:54
 * @note : It's not technology, it's art !
 **/
public interface CgroupsManager {

    /**
     * @Description : 添加资源配置计划
     * @Param :
     * @Return :
     * @Author : xyy
     * @Date : 2024/12/9
     * @note : An art cell !
     **/
    void createResourcePlan(ResourcePlanEntity plan);

    /**
     * @Description : 修改资源配置计划
     * @Param :
     * @Return :
     * @Author : xyy
     * @Date : 2024/12/9
     * @note : An art cell !
     **/
    void modifyResourcePlan(ResourcePlanEntity plan);

    /**
     * @Description : 删除资源配置计划
     * @Param :
     * @Return :
     * @Author : xyy
     * @Date : 2024/12/9
     * @note : An art cell !
     **/
    void delResourcePlan(String planId);

    /**
     * @Description : 根据id获取资源配置计划
     * @Param :
     * @Return :
     * @Author : xyy
     * @Date : 2024/12/11
     * @note : An art cell !
     **/
    ResourcePlanEntity getResourcePlanById(String id);


    void setCgroup(String serveName, double cpuQuota,double cpuShare,double memLimit,boolean interrupt);

    void unsetCgroup(String serveName);



}
