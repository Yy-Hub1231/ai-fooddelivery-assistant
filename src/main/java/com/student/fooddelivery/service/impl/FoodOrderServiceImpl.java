// 接口实现，4个@Tool函数（核心）

package com.student.fooddelivery.service.impl;

import com.student.fooddelivery.entity.FoodOrder;
import com.student.fooddelivery.mapper.FoodOrderMapper;
import com.student.fooddelivery.service.FoodOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Student
 * @version 1.0.0
 * @date 2026/9/16
 */
@Service
public class FoodOrderServiceImpl extends ServiceImpl<FoodOrderMapper, FoodOrder> implements FoodOrderService {

//    @Tool：SpringAI 注解，标记这个方法是大模型可调用的工具，description告诉AI这个函数是干嘛的
//    @ToolParam：描述每个入参的含义，大模型自动理解需要收集哪些信息；
//    逻辑：校验参数→封装订单→入库→返回文本结果。
    // 下单工具
    @Tool(description = "外卖下单/点餐")
    @Override
    public String placeOrder(
            @ToolParam(description = "用户名，中文，不能为空") String userName,
            @ToolParam(description = "菜品名称，不能为空") String foodName,
            @ToolParam(description = "数量，不能为空") Integer quantity,
            @ToolParam(description = "配送地址，不能为空") String address,
            @ToolParam(description = "联系电话，不能为空") String phone) {
        // 1.参数非空校验
        if (userName == null || foodName == null || quantity == null || address == null || phone == null) {
            return "参数不能为空，请提供完整的下单信息";
        }
        // 2.组装订单实体
        FoodOrder order = new FoodOrder();
        order.setUserName(userName);
        order.setFoodName(foodName);
        order.setQuantity(quantity);
        order.setPrice(new BigDecimal("25.00")); // 默认单价25元
        order.setAddress(address);
        order.setPhone(phone);
        order.setStatus("待配送");
        order.setOrderTime(LocalDateTime.now());
        // 3.保存到数据库
        boolean result = this.save(order);
        // 4.返回结果给大模型，大模型再包装成自然语言回复用户
        return result ? "下单成功！订单号：" + order.getId() + "，请耐心等待配送" : "下单失败，请稍后重试";
    }

    // 取消订单
    @Tool(description = "取消外卖订单/退单")
    @Override
    public String cancelOrder(
            @ToolParam(description = "订单id，不能为空") Integer id,
            @ToolParam(description = "用户名，中文，不能为空") String userName) {
        // 参数校验
        if (id == null || userName == null) {
            return "参数不正确，请提供订单号和用户名";
        }
        // 构造查询条件：同时匹配订单id + 用户名，防止别人乱取消他人订单
        LambdaQueryWrapper<FoodOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodOrder::getId, id)
                .eq(FoodOrder::getUserName, userName);
        FoodOrder order = this.getOne(wrapper);
        if (order == null) {
            return "订单不存在，请检查订单号和用户名是否正确";
        }
        // 业务规则：已配送的订单不能取消
        if ("已配送".equals(order.getStatus())) {
            return "订单已配送，无法取消";
        }
        // 修改状态为已取消
        order.setStatus("已取消");
        boolean result = this.updateById(order);
        return result ? "订单取消成功" : "取消失败，请稍后重试";
    }

    // 查询订单
    @Tool(description = "查询外卖订单")
    @Override
    public String queryOrders(
            @ToolParam(description = "用户名，中文，不能为空") String userName) {
        if (userName == null) {
            return "用户名不能为空";
        }
        // 查询该用户所有订单，按下单时间倒序
        LambdaQueryWrapper<FoodOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodOrder::getUserName, userName)
                .orderByDesc(FoodOrder::getOrderTime);
        List<FoodOrder> orderList = this.list(wrapper);
        if (orderList.isEmpty()) {
            return "您还没有任何订单";
        }
        // 拼接订单信息字符串，返回给大模型
        StringBuilder sb = new StringBuilder();
        sb.append("您的订单列表：\n");
        for (FoodOrder order : orderList) {
            sb.append(String.format("订单号：%d | 菜品：%s | 数量：%d | 状态：%s | 地址：%s | 时间：%s\n",
                    order.getId(), order.getFoodName(), order.getQuantity(),
                    order.getStatus(), order.getAddress(), order.getOrderTime()));
        }
        return sb.toString();
    }

    // 修改配送地址
    @Tool(description = "修改配送地址")
    @Override
    public String updateAddress(
            @ToolParam(description = "订单id，不能为空") Integer id,
            @ToolParam(description = "用户名，中文，不能为空") String userName,
            @ToolParam(description = "新的配送地址，不能为空") String newAddress) {
        if (id == null || userName == null || newAddress == null) {
            return "参数不正确，请提供订单号、用户名和新地址";
        }
        LambdaQueryWrapper<FoodOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodOrder::getId, id)
                .eq(FoodOrder::getUserName, userName);
        FoodOrder order = this.getOne(wrapper);
        if (order == null) {
            return "订单不存在，请检查订单号和用户名是否正确";
        }
        // 业务规则：已配送、已取消订单，不能修改地址
        if ("已配送".equals(order.getStatus()) || "已取消".equals(order.getStatus())) {
            return "订单状态为" + order.getStatus() + "，无法修改地址";
        }
        order.setAddress(newAddress);
        boolean result = this.updateById(order);
        return result ? "配送地址已更新为：" + newAddress : "修改失败，请稍后重试";
    }
}
