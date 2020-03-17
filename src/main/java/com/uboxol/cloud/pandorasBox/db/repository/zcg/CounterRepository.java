package com.uboxol.cloud.pandorasBox.db.repository.zcg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {
	
	//Counter findByGridId(String gridId);
	
	List<Counter> findByCabinetId(String cabinetId);
	
	List<Counter> findByCleanStatus(int cleanStatus);
	
	List<Counter> findByCabinetIdAndSpecsAndGridCurStatus(String cabinetId,String specs,int gridCurStatus);
	
	List<Counter> findByCabinetIdAndGridCurStatus(String cabinetId,int gridCurStatus);
	
	Counter findByCabinetIdAndGridId(String cabinetId,String gridId);

}
