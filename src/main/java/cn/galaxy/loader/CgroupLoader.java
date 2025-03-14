package cn.galaxy.loader;

import cn.gsq.common.AbstractInformationLoader;
import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 * Project : galaxy
 * Class : cn.galaxy.loader.CgroupLoader
 *
 * @author : xyy
 * @since : 2024-12-09 17:19
 **/
public class CgroupLoader extends AbstractInformationLoader {
    @Override
    public boolean isEnable() {
        return !System.getenv("ROLE").equals("server");
    }

    @Override
    public List<String> springBeansSupply() {
        return CollUtil.newArrayList("cn.gsq.cgroups.config");
    }
}
