package com.cwj.cwjojcodesandbox.security;

import java.security.Permission;

/**
 * 默认安全管理器
 */
public class DenySecurityManager extends SecurityManager {

    // 检查所有权限
    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限不足" + perm.toString());
    }
}