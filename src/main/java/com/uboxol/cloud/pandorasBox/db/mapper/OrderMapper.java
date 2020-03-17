package com.uboxol.cloud.pandorasBox.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.uboxol.cloud.pandorasBox.db.entity.zcg.Order;


/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/28 14:53
 */
@Mapper
public interface OrderMapper {

	 List<Order> findAll(Order order);
	 
}
