package net.gaox.occupation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> 占用大量端口实现 </p>
 *
 * @author gaox·Eric
 * @date 2020/5/20 21:32
 */
@Slf4j
@SpringBootApplication
public class OccupationApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(OccupationApplication.class, args);
        ExecutorService exec = Executors.newCachedThreadPool();

        Integer portSize = 200;

        for (int i = 0; i < portSize; i++) {
            final int portNum = i + 2000;
            // 排除8的倍数
            if (new Random().nextInt(3) == 0) {
                log.warn("now port is [{}]", portNum);
                continue;
            }

            exec.execute(() -> {
                try {
                    ServerSocket server = new ServerSocket(portNum);
                    log.info("server[{}]将一直等待连接的到来", portNum);
                    Socket socket = server.accept();

                    // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    StringBuilder sb = new StringBuilder();
                    //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
                    while ((len = inputStream.read(bytes)) != -1) {
                        // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                        sb.append(new String(bytes, 0, len, "UTF-8"));
                    }
                    System.out.println("get message from client: " + sb);

                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    server.close();

                } catch (Exception e) {
                    log.error("exception", e);
                }
            });
        }
        log.info("finish");
        exec.shutdown();
    }
}
