package com.kz.coderscircle.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体类，对应数据库中的user表。
 */
@Data
@TableName("user")
public class User implements UserDetails {

    @TableId(type = IdType.AUTO)
    private Long id;

    //用户名
    private String username;

    //密码
    @JsonIgnore
    private String password;

    //电子邮箱
    private String email;

    //昵称
    private String nickname;

    //用户头像URL
    @TableField("user_pic")
    private String userPic;

    /**
     * 账户状态: 0-未激活, 1-已激活, 2-已封禁
     */
    private Integer status;

    //用户角色。默认为"ROLE_USER"。
    private String role = "ROLE_USER";

    //创建时间
    @TableField("create_time")
    private LocalDateTime createTime;

    //更新时间
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 返回用户的权限集合。
     * Spring Security会使用此方法来获取用户的角色，用于@PreAuthorize等权限判断。
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 根据role字段返回用户的权限集合
        return List.of(new SimpleGrantedAuthority(role));
    }

    //账户是否未过期，项目中不涉及账户过期，直接返回true
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    //账户是否未被锁定目中不涉及账户锁定，直接返回true
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    //用户凭证（密码）是否未过期，直接返回true。
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //账户是否已启用
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.status != null && this.status == 1; // 只有当status为1时，账户才被认为是启用的
    }
}