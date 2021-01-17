package com.ydc.basepack.util;

import com.yukong.panda.common.async.ContextCopyingDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@SuppressWarnings(value = "Duplicates")

/**
 * 线程池管理： todo 真实项目中到底有线程池是单例的吗？
 */
public class ExecutorsManager {
    public static final String EXECUTOR_NAME = "MessageProducerExecutor";

    //Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
    @Primary
    @Bean(name=EXECUTOR_NAME)
    public Executor createExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(new ContextCopyingDecorator());
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("rabbit-producer-client-thread-");
        executor.initialize();
        return executor;
    }

}