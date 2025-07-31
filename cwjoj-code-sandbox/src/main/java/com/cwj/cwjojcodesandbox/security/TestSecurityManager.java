package com.cwj.cwjojcodesandbox.security;

import cn.hutool.core.io.FileUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestSecurityManager {
    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());

        List<String> strings = FileUtil.readLines("C:\\Users\\eveni\\Desktop\\Codes\\project\\cwjoj-code-sandbox\\src\\main\\resources\\application.yml", StandardCharsets.UTF_8);
        System.out.println(strings);
    }
}
