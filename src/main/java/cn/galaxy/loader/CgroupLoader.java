package cn.galaxy.loader;

import cn.gsq.common.AbstractInformationLoader;

/**
 * Project : galaxy
 * Class : cn.galaxy.loader.CgroupLoader
 *
 * @author : xyy
 * @date : 2024-12-09 17:19
 * @note : It's not technology, it's art !
 **/
public class CgroupLoader extends AbstractInformationLoader {
    @Override
    public boolean isEnable() {
        return !System.getenv("ROLE").equals("server");
    }
}
