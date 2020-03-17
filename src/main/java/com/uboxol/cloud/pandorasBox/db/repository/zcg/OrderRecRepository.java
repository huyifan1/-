package com.uboxol.cloud.pandorasBox.db.repository.zcg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uboxol.cloud.pandorasBox.db.entity.zcg.OrderRec;

@Repository
public interface OrderRecRepository extends JpaRepository<OrderRec, Long> {
	
	OrderRec findByOrderId(String orderId);

}
