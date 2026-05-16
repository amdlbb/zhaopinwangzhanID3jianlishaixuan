package com.abc.xyzp.config;


import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {
    @Bean
    public IRule ribbonRule() {
        System.out.println("使用NacosRule，优先选择本集群");
        return new NacosRule();
    }
}
