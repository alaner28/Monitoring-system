package com.sipc.monitoringsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sipc.monitoringsystem.model.po.User.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 17:32
 */

@Mapper
public interface UserDao extends BaseMapper<User> {
}
