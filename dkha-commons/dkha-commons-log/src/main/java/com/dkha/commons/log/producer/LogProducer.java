

package com.dkha.commons.log.producer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.dkha.commons.log.BaseLog;
import com.dkha.commons.tools.redis.RedisKeys;
import com.dkha.commons.tools.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 日志通过redis队列，异步保存到数据库
 * @since 1.0.0
 */
@Component
public class LogProducer {
    @Autowired
    private RedisUtils redisUtils;
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("log-producer-pool-%d").build();
    ExecutorService pool = new ThreadPoolExecutor(5, 200, 0L,TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 保存Log到Redis消息队列
     */
    public void saveLog(BaseLog log){
        String key = RedisKeys.getSysLogKey();

        //异步保存到队列
        pool.execute(() -> redisUtils.leftPush(key, log, RedisUtils.NOT_EXPIRE));
    }
}
