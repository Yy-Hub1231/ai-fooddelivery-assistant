// 订单实体类，映射数据库订单表
// MyBatis-Plus 实体，对应 mysql food_order 表

package com.student.fooddelivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data     // lombok注解，自动生成get/set toString
@AllArgsConstructor        // 全参构造
@NoArgsConstructor         // 无参构造
@TableName("food_order")   // 映射数据库表名food_order
public class FoodOrder {

    @TableId(type = IdType.AUTO)    //主键自增
    private Integer id;

    private String userName;        // 用户名

    private String foodName;        // 菜品名称

    private Integer quantity;       // 数量

    private BigDecimal price;       // 单价

    private String address;         // 配送地址

    private String phone;           // 联系电话

    private String status;          // 订单状态：待配送/已配送/已取消

    private LocalDateTime orderTime; // 下单时间
}
