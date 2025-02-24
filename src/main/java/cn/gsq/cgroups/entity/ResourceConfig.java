package cn.gsq.cgroups.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.entity.ResourceConfig
 *
 * @author : xyy
 * @date : 2024-12-09 10:58
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResourceConfig {

    private String serveName;

    private String processName;

    private int port;

    private double cpuLimit;

    private double memoryLimit;

    private double IOLimit;
}
