import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 运行危险命令
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/木马程序.bat";
        Process process = Runtime.getRuntime().exec(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String outputLine;
        while ((outputLine = bufferedReader.readLine()) != null) {
            System.out.println(outputLine);
        }
        System.out.println("执行程序成功");
    }
}