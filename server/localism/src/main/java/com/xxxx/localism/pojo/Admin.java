package com.xxxx.localism.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_admin")
public class Admin implements Serializable, UserDetails  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String  name;

    private String telephone;

    private String  username;

    private String  password;

    @TableField(exist = false)
    private Integer code;

    private boolean enabled;

    private String  userFace;

    private Boolean isExpert;

    private Boolean canCreateParty;

    private Integer partyId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
