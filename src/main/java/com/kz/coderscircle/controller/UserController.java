package com.kz.coderscircle.controller;

import com.kz.coderscircle.pojo.Result;
import com.kz.coderscircle.pojo.User;
import com.kz.coderscircle.service.FileStorageService;
import com.kz.coderscircle.service.UserService;
import com.kz.coderscircle.utils.JwtUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户核心功能接口：注册、激活、登录、获取及更新信息
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/register")
    public Result<Void> register(
            @Pattern(regexp = "^\\S{4,16}$", message = "用户名长度必须为4-16位")
            String username,
            @Pattern(regexp = "^\\S{6,16}$", message = "密码长度必须为6-16位")
            String password,
            @NotEmpty(message = "邮箱不能为空")
            @Email(message = "邮箱格式错误")
            String email) {
        userService.register(username, password, email);
        return Result.success();
    }

    @GetMapping("/activate")
    public String activate(String token) {
        boolean isSuccess = userService.activateAccount(token);
        if (isSuccess) {
            return "账户激活成功！您现在可以登录了。";
        } else {
            return "激活失败，链接无效或已过期。";
        }
    }

    @PostMapping("/login")
    public Result<String> login(
            @Pattern(regexp = "^\\S{4,16}$", message = "用户名长度必须为4-16位")
            String username,
            @Pattern(regexp = "^\\S{6,16}$", message = "密码长度必须为6-16位")
            String password
    ) {
        User loginUser = userService.findByUsername(username);
        if (loginUser == null) {
            return Result.error("用户名或密码错误");
        }
        if (loginUser.getStatus() != 1) {
            return Result.error("账户尚未激活或状态异常");
        }
        if (passwordEncoder.matches(password, loginUser.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = jwtUtil.genToken(claims);
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();
        return Result.success(loginUser);
    }

    @PutMapping("/nickname")
    public Result<Void> updateNickname(@RequestParam String nickname) {
        userService.updateNickname(nickname);
        return Result.success();
    }

    @PatchMapping("/password")
    public Result<Void> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        if (!StringUtils.hasLength(oldPassword) || !StringUtils.hasLength(newPassword)) {
            return Result.error("密码不能为空");
        }
        try {
            userService.updatePassword(oldPassword, newPassword);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PatchMapping("/avatar")
    public Result<String> updateAvatar(@RequestParam("avatar") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("上传的头像文件不能为空");
        }

        try {
            String fileName = fileStorageService.upload(file);
            String avatarUrl = "/uploads/" + fileName;
            userService.updateAvatar(avatarUrl);
            return Result.success(avatarUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败，请稍后重试");
        }
    }

    @PostMapping("/resetPasswordByEmailRequest")
    public Result<Void> resetPasswordByEmail(@RequestParam String email) {
        if (!StringUtils.hasLength(email)) {
            return Result.error("邮箱不能为空");
        }
        userService.resetPasswordByEmailRequest(email);
        return Result.success();
    }

    /**
     * 根据令牌执行密码重置的接口
     token 一次性重置令牌
     newPassword 新密码
     */
    @PostMapping("/resetPasswordByEmail")
    public Result<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!StringUtils.hasLength(token) || !StringUtils.hasLength(newPassword)) {
            return Result.error("令牌和新密码不能为空");
        }
        boolean success = userService.resetPasswordByEmail(token, newPassword);
        if (success) {
            return Result.success();
        } else {
            return Result.error("令牌无效或已过期，请重新请求");
        }
    }
}