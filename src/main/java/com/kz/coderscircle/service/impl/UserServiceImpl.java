package com.kz.coderscircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kz.coderscircle.mapper.UserMapper;
import com.kz.coderscircle.pojo.User;
import com.kz.coderscircle.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public User findByUsername(String username) {
        return userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public void register(String username, String password, String email) {
        if (findByUsername(username) != null) {
            throw new RuntimeException("用户名已被占用");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String token = UUID.randomUUID().toString();

        Map<String, String> preRegisteredData = new HashMap<>();
        preRegisteredData.put("username", username);
        preRegisteredData.put("password", encodedPassword);
        preRegisteredData.put("email", email);

        try {
            String userJson = objectMapper.writeValueAsString(preRegisteredData);
            String redisKey = "activate:user:" + token;
            redisTemplate.opsForValue().set(redisKey, userJson, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException("系统内部错误：序列化用户信息失败", e);
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("【码农圈】请激活您的账户");
        String activationUrl = "http://localhost:8080/user/activate?token=" + token;
        message.setText("欢迎注册码农圈！请点击以下链接激活您的账户（链接30分钟内有效）：\n" + activationUrl);

        mailSender.send(message);
    }

    @Override
    public boolean activateAccount(String token) {
        String redisKey = "activate:user:" + token;
        String userJson = redisTemplate.opsForValue().get(redisKey);

        if (userJson == null) {
            log.warn("账户激活失败：无效或已过期的Token: {}", token);
            return false;
        }

        try {
            Map<String, String> data = objectMapper.readValue(userJson, new TypeReference<>() {});
            String username = data.get("username");

            if (findByUsername(username) != null) {
                log.warn("账户激活失败：用户名 '{}' 在激活期间已被注册。", username);
                return false;
            }

            User userToActivate = new User();
            userToActivate.setUsername(username);
            userToActivate.setPassword(data.get("password"));
            userToActivate.setEmail(data.get("email"));
            userToActivate.setStatus(1);

            userMapper.insert(userToActivate);
            redisTemplate.delete(redisKey);

            return true;
        } catch (Exception e) {
            log.error("激活账户时发生意外错误, Token: {}", token, e);
            return false;
        }
    }

    @Override
    public void updateNickname(String nickname) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loginUser.setNickname(nickname);
        loginUser.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(loginUser);
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!passwordEncoder.matches(oldPassword, loginUser.getPassword())) {
            throw new RuntimeException("原密码不正确");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        loginUser.setPassword(encodedNewPassword);
        loginUser.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(loginUser);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loginUser.setUserPic(avatarUrl);
        loginUser.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(loginUser);
    }

    //根据邮箱查找用户
    public User findByEmail(String email) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }

    @Override
    public void resetPasswordByEmailRequest(String email) {
        // 根据邮箱查找用户
        User user = findByEmail(email);

        // 验证用户是否存在
        if (user != null) {
            String token = UUID.randomUUID().toString();
            String redisKey = "reset:password:user:" + token;

            //token存入redis
            redisTemplate.opsForValue().set(redisKey, user.getId().toString(), 15, TimeUnit.MINUTES);

            //发重置密码邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("【码农圈】重置密码");
            String emailText = String.format(
                    "您好，\n\n您正在请求重置密码。请在15分钟内，使用以下令牌和您的新密码，调用 POST /user/resetPasswordByEmail 接口来完成操作。\n\n您的重置令牌是: %s\n\n请勿将此令牌泄露给他人。\n\n码农圈团队",
                    token
            );
            message.setText(emailText);
            mailSender.send(message);
        }
    }

    @Override
    public boolean resetPasswordByEmail(String token, String newPassword) {
        String redisKey = "reset:password:user:" + token;
        // 从Redis中查找令牌
        String userId = redisTemplate.opsForValue().get(redisKey);

        if (userId != null) {
            // 加密新密码
            String encodedNewPassword = passwordEncoder.encode(newPassword);

            // 更新数据库中的密码
            User userToUpdate = new User();
            userToUpdate.setId(Long.parseLong(userId));
            userToUpdate.setPassword(encodedNewPassword);
            userToUpdate.setUpdateTime(LocalDateTime.now());

            // 更新用户
            userMapper.updateById(userToUpdate);

            // 从Redis中删除已使用的令牌，确保一次性
            redisTemplate.delete(redisKey);
            return true;
        }

        // 如果令牌不存在或已过期，返回失败
        return false;
    }
}