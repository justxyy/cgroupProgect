package cn.gsq.cgroups.config;

import cn.gsq.cgroups.CgroupManagerImpl;
import cn.gsq.cgroups.CgroupsManager;
import cn.gsq.cgroups.quartz.DefaultStaticResourceScheduler;
import cn.gsq.cgroups.quartz.StaticResourceScheduler;
import cn.hutool.core.lang.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Project : galaxy
 * Class : cn.gsq.cgroups.config.CgroupsAutoConfigure
 *
 * @author : xyy
 * @since : 2024-11-05 09:55
 **/
@Configuration
public class CgroupsAutoConfigure {

    @Bean(name = "cgroupsManager")
    public CgroupsManager getCgroupsManager(){
        return new CgroupManagerImpl();
    }

    @Bean(name = "staticResourceScheduler")
    public StaticResourceScheduler getStaticResourceScheduler(){
        return new DefaultStaticResourceScheduler();
    }

    @Bean(name = "cgroupEnvLoad")
    public Pair<String, String> load(CgroupsManager manager){
        ((CgroupManagerImpl) manager).init();
        return null;
    }
}
