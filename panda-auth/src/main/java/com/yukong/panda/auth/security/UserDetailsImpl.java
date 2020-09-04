package com.yukong.panda.auth.security;

import com.yukong.panda.common.entity.SysUser;
import com.yukong.panda.common.enums.UserStatusEnum;
import com.yukong.panda.common.vo.SysRoleVo;
import com.yukong.panda.common.vo.SysUserVo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: yukong
 * @date: 2018/10/9 10:09
 * @description: security 封装用户对象
 */
@Data
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = -2636609458742965698L;

    private Integer userId;
    private String username;
    private String password;
    private String status;
    private List<SysRoleVo> roleVos;




    public UserDetailsImpl(SysUserVo userVo) {
        this.userId = userVo.getUserId();
        this.username = userVo.getUsername();
        this.password = userVo.getPassword();
        this.status = userVo.getDelFlag();
        this.roleVos = userVo.getSysRoleVoList();
    }

    /**
     * 根据用户的角色获取Authority
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        roleVos.forEach(role ->{
            authorityList.add(new SimpleGrantedAuthority(role.getRoleCode()));
        });
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !StringUtils.equals(UserStatusEnum.LOCK.getCode(), status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.equals(UserStatusEnum.NORMAL.getCode(), status);
    }
}
