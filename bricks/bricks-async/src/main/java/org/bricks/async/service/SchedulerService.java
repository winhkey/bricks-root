package org.bricks.async.service;

import static java.time.Instant.now;

import javax.annotation.Resource;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

/**
 * 定时执行
 *
 * @author fuzhying
 *
 */
@Service
public class SchedulerService
{

    /**
     * 任务池
     */
    @Resource
    private ThreadPoolTaskScheduler scheduler;

    /**
     * 定时器缓存
     */
    @Resource
    private ScheduledFutureCacheService scheduledFutureCacheService;

    /**
     * 定时执行
     *
     * @param key 任务名
     * @param task 任务线程
     * @param millisecond 间隔
     */
    public void schedule(String key, Runnable task, long millisecond)
    {
        cancel(key);
        scheduledFutureCacheService.put(key, scheduler.scheduleWithFixedDelay(task, millisecond));
    }

    /**
     * 延迟执行
     *
     * @param key 任务名
     * @param task 任务线程
     * @param second 延迟
     */
    public void scheduleAfter(String key, Runnable task, long second)
    {
        cancel(key);
        scheduledFutureCacheService.put(key, scheduler.schedule(task, now().plusSeconds(second)));
    }

    /**
     * 取消定时任务
     *
     * @param key 任务名
     */
    public void cancel(String key)
    {
        scheduledFutureCacheService.cancel(key, true);
    }

}
