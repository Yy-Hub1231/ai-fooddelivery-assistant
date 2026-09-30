// 测试接口

package com.student.fooddelivery.controller;

import com.student.fooddelivery.entity.FoodOrder;
import com.student.fooddelivery.service.FoodOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {

    @Autowired
    FoodOrderService foodOrderService;

    // POST /test/order 下单接口，接收json参数
    @PostMapping("/order")
    public Map<String, Object> testPlaceOrder(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从请求体取出参数
            String userName = (String) params.get("userName");
            String foodName = (String) params.get("foodName");
            // quantity判空，如果没有传默认1
            Integer quantity = params.get("quantity") != null ? Integer.parseInt(params.get("quantity").toString()) : 1;
            String address = (String) params.get("address");
            String phone = (String) params.get("phone");

            // 调用service下单
            String msg = foodOrderService.placeOrder(userName, foodName, quantity, address, phone);
            result.put("success", true);
            result.put("message", msg);
        } catch (Exception e) {
            // 捕获异常，返回错误信息
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("error", e.getClass().getName());
            if (e.getCause() != null) {
                result.put("cause", e.getCause().getMessage());
            }
        }
        return result;
    }

    // GET /test/list 查询全部订单列表
    @GetMapping("/list")
    public Map<String, Object> testList() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("data", foodOrderService.list());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
