package net.gaox.random.os;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * <p> 操作系统类 </p>
 * 获取System.getProperty("os.name")对应的操作系统
 *
 * @author gaox·Eric
 * @date 2020/5/21 00:36
 */
@Slf4j
public class OSInfo {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    private static OSInfo _instance = new OSInfo();

    private PlatformEnum platform;

    private OSInfo() {
    }

    public static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
    }

    public static boolean isMacOSX() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public static boolean isOS2() {
        return OS.indexOf("os/2") >= 0;
    }

    public static boolean isSolaris() {
        return OS.indexOf("solaris") >= 0;
    }

    public static boolean isSunOS() {
        return OS.indexOf("sunos") >= 0;
    }

    public static boolean isMPEiX() {
        return OS.indexOf("mpe/ix") >= 0;
    }

    public static boolean isHPUX() {
        return OS.indexOf("hp-ux") >= 0;
    }

    public static boolean isAix() {
        return OS.indexOf("aix") >= 0;
    }

    public static boolean isOS390() {
        return OS.indexOf("os/390") >= 0;
    }

    public static boolean isFreeBSD() {
        return OS.indexOf("freebsd") >= 0;
    }

    public static boolean isIrix() {
        return OS.indexOf("irix") >= 0;
    }

    public static boolean isDigitalUnix() {
        return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.indexOf("netware") >= 0;
    }

    public static boolean isOSF1() {
        return OS.indexOf("osf1") >= 0;
    }

    public static boolean isOpenVMS() {
        return OS.indexOf("openvms") >= 0;
    }

    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static PlatformEnum getOSname() {
        if (isAix()) {
            _instance.platform = PlatformEnum.AIX;
        } else if (isDigitalUnix()) {
            _instance.platform = PlatformEnum.Digital_Unix;
        } else if (isFreeBSD()) {
            _instance.platform = PlatformEnum.FreeBSD;
        } else if (isHPUX()) {
            _instance.platform = PlatformEnum.HP_UX;
        } else if (isIrix()) {
            _instance.platform = PlatformEnum.Irix;
        } else if (isLinux()) {
            _instance.platform = PlatformEnum.Linux;
        } else if (isMacOS()) {
            _instance.platform = PlatformEnum.Mac_OS;
        } else if (isMacOSX()) {
            _instance.platform = PlatformEnum.Mac_OS_X;
        } else if (isMPEiX()) {
            _instance.platform = PlatformEnum.MPEiX;
        } else if (isNetWare()) {
            _instance.platform = PlatformEnum.NetWare_411;
        } else if (isOpenVMS()) {
            _instance.platform = PlatformEnum.OpenVMS;
        } else if (isOS2()) {
            _instance.platform = PlatformEnum.OS2;
        } else if (isOS390()) {
            _instance.platform = PlatformEnum.OS390;
        } else if (isOSF1()) {
            _instance.platform = PlatformEnum.OSF1;
        } else if (isSolaris()) {
            _instance.platform = PlatformEnum.Solaris;
        } else if (isSunOS()) {
            _instance.platform = PlatformEnum.SunOS;
        } else if (isWindows()) {
            _instance.platform = PlatformEnum.Windows;
        } else {
            _instance.platform = PlatformEnum.Others;
        }
        return _instance.platform;
    }

    public static Boolean portUsed(Integer port) {
        String[] netStatStr;
        StringBuilder sb = null;
        PlatformEnum oSname = getOSname();

        // Mac
        if (PlatformEnum.Mac_OS_X.equals(oSname) || PlatformEnum.Mac_OS.equals(oSname)) {
            netStatStr = new String[]{"/bin/sh", "-c", "netstat -an | grep " + port};
        } else if (PlatformEnum.Windows.equals(oSname)) {
            // Windows
            netStatStr = new String[]{"cmd", "-c", "netstat -ano | findstr " + port};

        } else {
            // 默认 Linux
            netStatStr = new String[]{"/bin/sh", "-c", "netstat -ano | grep " + port};
        }

        try {
            // 利用Runtime类获得运行时环境去执行一个netstat的命令
            // 该exec()方法返回的Process类型数据有一个getInputStream()方法，获得其输入流拿到命令查询结果字符串
            InputStream inputstream = Runtime.getRuntime().exec(netStatStr).getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));

            // sb就是最后拿到的查询结果，最后可以处理字符串拿到占用这个端口号的外部ip
            sb = new StringBuilder();
            String temp;
            while (null != (temp = bufferedReader.readLine())) {
                sb.append(temp + "\n");
            }
        } catch (IOException e) {
            log.error("", e);
            return false;
        }

        sb.trimToSize();
        log.debug("netStat length={}\n{}", sb.length(), sb.toString());
        return sb.length() > 2;
    }


    /**
     * 获取可用端口
     *
     * @return port
     */
    public static int getAvailablePort() {
        int max = 65535;
        int min = 2000;

        Random random = new Random();
        int port = random.nextInt(max) % (max - min + 1) + min;
        boolean using = portUsed(port);
        if (using) {
            return getAvailablePort();
        } else {
            return port;
        }
    }

    /**
     * 配置端口
     *
     * @param args
     */
    public static void randomOrStartPort(String[] args) {
        Boolean isServerPort = false;
        String serverPort = "";
        if (args != null) {
            for (String arg : args) {
                if (StringUtils.hasText(arg) &&
                        arg.startsWith("--server.port")
                ) {
                    isServerPort = true;
                    serverPort = arg;
                    break;
                }
            }
        }

        //没有指定端口，则随机生成一个可用的端口
        if (!isServerPort) {
            int port = getAvailablePort();
            log.info("current server.port=" + port);
            System.setProperty("server.port", String.valueOf(port));
        } else {
            //指定了端口，则以指定的端口为准
            log.info("current server.port=" + serverPort.split("=")[1]);
            System.setProperty("server.port", serverPort.split("=")[1]);
        }
    }

    /**
     * Test
     *
     * @param args args
     */
    public static void main(String[] args) {
        System.out.println(getOSname());
        System.out.println("------------ origin data ----------------");
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("os.version"));
        System.out.println(System.getProperty("os.arch"));
    }
}
