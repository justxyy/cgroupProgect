package cn.gsq.cgroups.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.entity.ResourcePlanEntity
 *
 * @author : xyy
 * @since : 2024-12-09 14:46
 **/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResourcePlanEntity {

    private String id;

    private String name;

    private String description;

    private boolean enabled;

    private String startCronExp;

    private String endCronExp;

    private String createTime;

    private List<ResourceConfig> resourceConfigs;

}
