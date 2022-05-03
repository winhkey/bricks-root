package org.bricks.async.service;

import static java.util.Optional.ofNullable;

import java.util.concurrent.ScheduledFuture;

import org.bricks.service.AbstractCacheMapService;
import org.springframework.stereotype.Component;

/**
 * 定时任务缓存
 *
 * @author fuzhiying
 *
 */
@Component
public class ScheduledFutureCacheService extends AbstractCacheMapService<ScheduledFuture<?>>
{

    /**
     * 删除并取消任务
     *
     * @param key key
     * @param interrupt 运行中断
     */
    public void cancel(String key, boolean interrupt)
    {
        ofNullable(get(key)).filter(s -> s.cancel(interrupt))
                .ifPresent(s -> remove(key));
    }

}
