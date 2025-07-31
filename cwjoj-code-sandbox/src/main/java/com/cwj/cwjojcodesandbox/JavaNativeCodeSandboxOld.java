package com.cwj.cwjojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.cwj.cwjojcodesandbox.model.ExecuteCodeRequest;
import com.cwj.cwjojcodesandbox.model.ExecuteCodeResponse;
import com.cwj.cwjojcodesandbox.model.ExecuteMessage;
import com.cwj.cwjojcodesandbox.model.JudgeInfo;
import com.cwj.cwjojcodesandbox.utils.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaNativeCodeSandboxOld implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;

    public static final String SECURITY_MANAGER_PATH = "C:\\Users\\eveni\\Desktop\\Codes\\project\\cwjoj-code-sandbox\\src\\main\\resources\\security";

    public static final String SECURITY_MANAGER_CLASSNAME = "MySecurityManager";

    private static final List<String> blackList = Arrays.asList("Files", "exec");

    public static final WordTree wordTree;

    static {
        // 初始化字典树
        wordTree = new WordTree();
        wordTree.addWords(blackList);
    }

    public static void main(String[] args) {
        JavaNativeCodeSandboxOld javaNativeCodeSandbox = new JavaNativeCodeSandboxOld();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
//        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/SleepError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/MemoryError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/ReadFileError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/WriteFileError.java", StandardCharsets.UTF_8);
        String code = ResourceUtil.readStr("testCode/unsafeCode/RunFileError.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        // 校验代码，代码中是否包含黑名单中的命令
        FoundWord foundWord = wordTree.matchWord(code);
        if (foundWord != null) {
            System.out.println("包含敏感词：" + foundWord.getFoundWord());
            return null;
        }

        // 把用户代码保存为文件
        // 获取当前项目地址
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码目录是否存在
        if (FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 编译代码
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (IOException e) {
            return getErrorResponse(e, userCodeFile, userCodeParentPath);
        }

        // 执行代码，得到输出
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASSNAME, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("代码执行超时，中断");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        delFile(userCodeFile, userCodeParentPath);
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                executeMessageList.add(executeMessage);
                System.out.println(executeMessage);
            } catch (IOException e) {
                return getErrorResponse(e, userCodeFile, userCodeParentPath);
            }
        }

        // 整理输出拿到结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        // 采用执行时间最大值统计执行时间
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 执行代码存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }
        if (outputList.size() == executeMessageList.size()) {
            // 正常执行完成
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        // 使用命令行获取执行小号内存非常麻烦，不做实现
//        judgeInfo.setMemory();
        executeCodeResponse.setJudgeInfo(judgeInfo);

        // 文件清理
        delFile(userCodeFile, userCodeParentPath);
        return executeCodeResponse;
    }

    /**
     * 错误处理，提升程序健壮性
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e, File userCodeFile, String userCodeParentPath) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        // 清理文件
        delFile(userCodeFile, userCodeParentPath);
        return executeCodeResponse;
    }

    private boolean delFile(File userCodeFile, String userCodeParentPath) {
        boolean del = false;
        if (userCodeFile.getParentFile() != null) {
            del = FileUtil.del(userCodeParentPath);
        }
        System.out.println("删除" + (del ? "成功" : "失败"));
        return del;
    }
}
