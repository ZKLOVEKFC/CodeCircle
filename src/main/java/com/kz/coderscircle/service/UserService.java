package com.kz.coderscircle.service;

import com.kz.coderscircle.pojo.User;

public interface UserService {

    /**
     * 根据用户名查询用户
     username 用户名
     return一个user对象，如果不存在则返回null
     */
    User findByUsername(String username);

    /**
     * 预注册新用户（存入Redis并发送激活邮件）
     username 用户名
     password 明文密码
     email 邮箱
     */
    void register(String username, String password, String email);

    /**
     * 根据激活令牌激活用户账户（从Redis取数据并存入数据库）
     token 激活令牌
     返回boolean是否激活成功
     */
    boolean activateAccount(String token);

    /**
     * 更新当前登录用户的昵称
     nickname 新的昵称
     */
    void updateNickname(String nickname);

    /**
     * 更新当前登录用户的密码
     oldPassword 旧密码
     newPassword 新密码
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 更新当前登录用户的头像
     avatarUrl 新头像的URL地址
     */
    void updateAvatar(String avatarUrl);

    /**
     * 根据邮箱查找用户
     email 邮箱地址
     User对象或null
     */
    User findByEmail(String email);

    /**
     * 请求密码重置，发送邮件
     email 用户注册的邮箱
     */
    void resetPasswordByEmailRequest(String email);

    /**
     * 根据令牌执行密码重置
     token       一次性重置令牌
     newPassword 新的明文密码
     */
    boolean resetPasswordByEmail(String token, String newPassword);
}