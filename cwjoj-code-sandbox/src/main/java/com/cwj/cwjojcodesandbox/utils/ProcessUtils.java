package com.cwj.cwjojcodesandbox.utils;


import com.cwj.cwjojcodesandbox.model.ExecuteMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 进程工具类
 */
public class ProcessUtils {

    /**
     * 执行进程并获取信息
     *
     * @param runProcess
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 等待程序执行获得退出码
            int exitValue = runProcess.waitFor();
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
            executeMessage.setExitValue(exitValue);
            // 正常退出
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 分批获取程序正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String outputLine;
                while ((outputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(outputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));
            } else {
                System.out.println(opName + "失败，错误码为：" + exitValue);
                // 分批获取程序正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String outputLine;
                while ((outputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(outputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));

                // 分批获取进程输出
                BufferedReader errorbufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                List<String> errorOutputStrList = new ArrayList<>();
                // 逐行读取
                String errorOutputLine;
                while ((errorOutputLine = errorbufferedReader.readLine()) != null) {
                    errorOutputStrList.add(errorOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(errorOutputStrList, "\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}

