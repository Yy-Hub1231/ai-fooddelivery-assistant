// MyBatis-Plus 继承`BaseMapper`直接拥有基础数据库操作， Mapper数据访问层

package com.student.fooddelivery.mapper;

import com.student.fooddelivery.entity.FoodOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodOrderMapper extends BaseMapper<FoodOrder> {
    // BaseMapper自带CRUD方法：insert、delete、update、selectList，不用写xmlsql
}
