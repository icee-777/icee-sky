package com.icee.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.icee.constant.MessageConstant;
import com.icee.dto.UserLoginDTO;
import com.icee.entity.User;
import com.icee.exception.BaseException;
import com.icee.exception.LoginFailedException;
import com.icee.mapper.UserMapper;
import com.icee.service.UserService;
import com.icee.utils.HttpClientUtil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User weixinLogin(UserLoginDTO userLoginDTO) {
        //调用微信接口,获取openid
        Map<String,String> param=new HashMap<>();
        param.put("appid","wxbe614716bb010803");
        param.put("secret","e2cdcd30ae5d3a69e4acbbfbf4aef36f");
        param.put("js_code",userLoginDTO.getCode());
        param.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", param);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = (String)jsonObject.get("openid");


        //判断openid是否为空
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断是否为新用户
        User user=userMapper.getByOpenid(openid);

        //新用户自动完成注册
        if(user==null){
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}
