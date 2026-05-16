package com.abc.xyzp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public Executor mailTaskExecutor() {

        //ThreadPoolTaskExecutor是线程池的springboot实现类
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);          // 核心线程数
        executor.setMaxPoolSize(20);           // 最大线程数
        executor.setQueueCapacity(100);        // 任务队列容量
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("mail-send-");
        // 拒绝策略：当队列和最大池都满时，由调用线程执行（保证不丢任务）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
