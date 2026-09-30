// 订单业务接口，定义4个工具方法，这个接口会被 SpringAI 识别为工具，AI 自动调用里面 4 个方法。
// 接口的实现类`FoodOrderServiceImpl`，里面方法上加`@Tool`注解，SpringAI 才会识别为工具函数。

package com.student.fooddelivery.service;

import com.student.fooddelivery.entity.FoodOrder;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FoodOrderService extends IService<FoodOrder> {

    /**
     * 下单
     */
    public String placeOrder(String userName, String foodName, Integer quantity, String address, String phone);

    /**
     * 取消订单
     */
    public String cancelOrder(Integer id, String userName);

    /**
     * 查询订单
     */
    public String queryOrders(String userName);

    /**
     * 修改配送地址
     */
    public String updateAddress(Integer id, String userName, String newAddress);
}
