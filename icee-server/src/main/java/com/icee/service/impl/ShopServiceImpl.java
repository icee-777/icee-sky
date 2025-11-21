package com.icee.service.impl;

import com.icee.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ShopServiceImpl implements ShopService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取营业状态
     * @return
     */
    @Override
    public Integer getStatus() {
        String status = redisTemplate.opsForValue().get("SHOP_STATUS");
        return Integer.parseInt(status);
    }

    /**
     * 设置营业状态
     * @param status
     */
    @Override
    public void setStatus(Integer status) {
        redisTemplate.opsForValue().set("SHOP_STATUS",status.toString());
    }
}
