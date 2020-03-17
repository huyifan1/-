package com.uboxol.cloud.pandorasBox.service;

import com.uboxol.cloud.cfg.UboxCloudThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/11/5 14:33
 */
@Slf4j
@Service
public class ThreadService {

    private static final ThreadPoolExecutor CORE_EXECUTOR = new ThreadPoolExecutor(2, 8,
                                                                                   60L, TimeUnit.SECONDS,
                                                                                   new SynchronousQueue<>(),
                                                                                   new UboxCloudThreadFactory(
                                                                                       "core"));

    private static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(32, 128,
                                                                                   30L, TimeUnit.SECONDS,
                                                                                   new LinkedBlockingQueue<>(128),
                                                                                   new UboxCloudThreadFactory(
                                                                                       "mermaid"));

    private static final ThreadPoolExecutor MINOR_EXECUTOR = new ThreadPoolExecutor(1, 2,
                                                                                    30L, TimeUnit.SECONDS,
                                                                                    new LinkedBlockingQueue<>(128),
                                                                                    new UboxCloudThreadFactory(
                                                                                        "minor"));

    public static final ThreadPoolExecutor EXCEL_EXECUTOR = new ThreadPoolExecutor(1, 12,
                                                                                   30L, TimeUnit.SECONDS,
                                                                                   new SynchronousQueue<>(),
                                                                                   new UboxCloudThreadFactory(
                                                                                       "excel"));

    public static int getActiveCount() {
        return POOL_EXECUTOR.getActiveCount();
    }

    public static boolean check() {
        int activeCount = POOL_EXECUTOR.getActiveCount();
        int poolSize = POOL_EXECUTOR.getMaximumPoolSize();

        logger.debug("线程池总数:{},当前活动:{}", poolSize, activeCount);

        return poolSize > activeCount;
    }

    /**
     * 主要线程执行
     *
     * @param task Runnable
     */
    public void exec(Runnable task) {
        execute(task);
    }

    /**
     * 主要线程执行
     *
     * @param task Runnable
     */
    public static void execute(Runnable task) {
        POOL_EXECUTOR.execute(task);
        logger.debug("{}/{} 添加新任务:{}", POOL_EXECUTOR.getActiveCount(),
                     POOL_EXECUTOR.getCorePoolSize(),
                     task.toString());
    }

    /**
     * 次要线程任务,不怎么重要,不保证执行
     *
     * @param task
     */
    public void execMinor(Runnable task) {
        MINOR_EXECUTOR.execute(task);
        logger.debug("{}/{} 添加次要单线程 新任务:{}", MINOR_EXECUTOR.getActiveCount(),
                     MINOR_EXECUTOR.getCorePoolSize(),
                     task.toString());
    }

    public static Future submit(Runnable task) {

        logger.debug("提交新任务:{}", task.toString());

        return POOL_EXECUTOR.submit(task);
    }

    /**
     * 提交核心任务,非阻塞,数量少,以免普通线程池中排队,影响执行
     *
     * @param task 重要任务
     */
    public void core(Runnable task) {

        logger.warn("添加重要新任务:{} ,非核心任务请使用普通线程池", task.toString());

        CORE_EXECUTOR.execute(task);
    }

    @PreDestroy
    public void destroy() {

        CORE_EXECUTOR.shutdownNow();

        logger.debug("CORE_EXECUTOR 销毁线程池:{}", CORE_EXECUTOR.isShutdown());

        POOL_EXECUTOR.shutdownNow();

        logger.debug("POOL_EXECUTOR 销毁线程池:{}", POOL_EXECUTOR.isShutdown());

        EXCEL_EXECUTOR.shutdownNow();

        logger.debug("EXCEL_EXECUTOR 销毁线程池:{}", EXCEL_EXECUTOR.isShutdown());
    }
}
