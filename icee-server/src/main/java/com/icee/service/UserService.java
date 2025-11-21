package com.icee.service;

import com.icee.dto.UserLoginDTO;
import com.icee.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User weixinLogin(UserLoginDTO userLoginDTO);
}
