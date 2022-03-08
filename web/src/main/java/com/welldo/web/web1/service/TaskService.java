package com.welldo.web.web1.service;


import com.welldo.web.web1.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 1.无需额外的依赖，我们可以直接在AppConfig中加上@EnableScheduling就开启了定时任务的支持：
 * 2.编写一个public void无参数方法，然后加上@Scheduled注解：
 *
 * 如果没有看到定时任务的日志，需要检查：
 * 是否忘记了在 {@link AppConfig} 中标注@EnableScheduling；
 * 是否忘记了在定时任务的方法所在的class标注@Component。
 */


// @Component
public class TaskService {

    final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 1. 启动延迟5秒，并以1秒的间隔执行任务
     * 除了可以使用 fixedRate 外，还可以使用 fixedDelay
     *
     * FixedRate是指任务总是以固定时间间隔触发，不管任务执行多长时间：
     * │░░░░   │░░░░░░ │░░░    │░░░░░  │░░░
     * ├───────┼───────┼───────┼───────┼────>
     * │<─────>│<─────>│<─────>│<─────>│
     *
     * 而FixedDelay是指，上一次任务执行完毕后，等待固定的时间间隔，再执行下一次任务：
     *
     * │░░░│       │░░░░░│       │░░│       │░
     * └───┼───────┼─────┼───────┼──┼───────┼──>
     *     │<─────>│     │<─────>│  │<─────>│
     *
     */
    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public void checkSystemStatusEveryMinute() {
        logger.info("Start check system status...");

        //模拟任务失败
        if (Math.random() > 0.8) {
            throw new RuntimeException("check system status task failed.");
        }
    }

    /**
     * 2.
     * 因为Java的注解全部是常量，写死了fixedDelay=5_000，如果根据实际情况要改成 10 秒怎么办，只能重新编译？
     * 我们可以把定时任务的配置放到配置文件中，
     * 例如task.properties：     * task.checkDiskSpace=10_000
     * 在代码中，我们需要用fixedDelayString取代fixedDelay：
     * 这样，就只用重启，无需重新编译代码。
     *
     * 2.5
     * fixedDelayString还可以使用更易读的Duration，例如：
     * @Scheduled(initialDelay = 30_000, fixedDelayString = "${task.checkDiskSpace:PT2M30S}")
     * 以字符串PT2M30S表示的Duration就是2分30秒
     */
    @Scheduled(initialDelay = 5_000, fixedDelayString = "${task.checkDiskSpace:30000}")
    public void checkDiskSpaceEveryMinute() {
        logger.info("Start check disk space...");
    }


    /**
     * 3.还有一类定时任务，它不是简单的重复执行，而是按时间触发，我们把这类任务称为Cron任务
     *
     *  在Spring中，它的格式是：（年是可以忽略的，通常不写）
     * 秒 分 小时 天 月份 星期 年
     *
     * 1，每天凌晨2:15执行的Cron表达式就是：
     * 0 15 2 * * *
     *
     * 2，每个工作日12:00执行的Cron表达式就是：
     * 0 0 12 * * MON-FRI
     *
     * 3，每个月1号，2号，3号和10号12:00执行的Cron表达式就是：
     * 0 0 12 1-3,10 * *
     */
    @Scheduled(cron = "${task.report:0 15 2 * * *}")//举例：我们定义一个每天凌晨2:15执行的任务：
    public void cronDailyReport() {
        logger.info("Start daily report task...");
    }

    //4，Cron表达式还可以表达每10分钟执行，例如：
    // 0 */10 * * * *
    //它可以取代fixedRate类型的定时任务。

}

