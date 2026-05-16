package com.abc.xyzp.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolMonitorUtils {

    public static void printPoolStatus(ThreadPoolTaskExecutor executor, String trigger) {
        if (executor == null) return;
        ThreadPoolExecutor pool = executor.getThreadPoolExecutor();
        System.out.printf("【%s】活跃线程数=%d, 核心线程数=%d, 最大线程数=%d, 队列任务数=%d, 已完成任务数=%d%n",
                trigger,
                pool.getActiveCount(),
                pool.getCorePoolSize(),
                pool.getMaximumPoolSize(),
                pool.getQueue().size(),
                pool.getCompletedTaskCount());
    }
}