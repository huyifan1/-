package com.uboxol.cloud.pandorasBox.db.repository.zcg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	Order findByOrderId(String orderId);

}
