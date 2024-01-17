package top.zion.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author zion
 * @since 2024/1/17 10:01:13
 */
public class CommandUtil {
    public static int doCmd(String command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
        pb.redirectErrorStream(true);
        pb.environment().put("LANG", "en_US.UTF-8");
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        return process.waitFor();
    }
}
