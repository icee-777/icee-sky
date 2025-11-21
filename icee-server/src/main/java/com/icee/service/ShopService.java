package com.icee.service;

public interface ShopService {
    /**
     * 获取营业状态
     * @return
     */
    Integer getStatus();

    /**
     * 设置营业状态
     * @param status
     */
    void setStatus(Integer status);
}
