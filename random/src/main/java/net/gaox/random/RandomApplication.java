package net.gaox.random;

import lombok.extern.slf4j.Slf4j;
import net.gaox.random.os.OSInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

/**
 * <p> 系统启动时使用随机端口 </p>
 *
 * @author gaox·Eric
 * @date 2020/5/20 21:36
 */
@Slf4j
@SpringBootApplication
public class RandomApplication {

    public static void main(String[] args) {
        int port = new Random().nextInt(200) + 2000;
        log.info("random.int(2000,2200) is [{}]", port);
        if (OSInfo.portUsed(port)) {
            log.error("[{}] is used!", port);
        } else {
            log.info("app was in [{}] start!", port);
        }
        SpringApplication.run(RandomApplication.class, args);
    }
}
