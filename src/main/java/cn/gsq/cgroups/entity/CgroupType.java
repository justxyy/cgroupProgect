package cn.gsq.cgroups.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.entity.CgroupType
 *
 * @author : xyy
 * @date : 2024-11-06 09:10
 * @note : It's not technology, it's art !
 **/
@Getter
@AllArgsConstructor
public enum CgroupType {

    CPU("cpu"),

    MEMORY("memory");

    private String type;
}
