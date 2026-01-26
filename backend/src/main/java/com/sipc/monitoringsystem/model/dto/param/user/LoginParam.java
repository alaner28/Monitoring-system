package com.sipc.monitoringsystem.model.dto.param.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 20:54
 */
@Data
public class LoginParam {
    @NotNull(message = "username不能为空")
    public String userName;

    @NotNull(message = "password不能为空")
    public String password;
}
