package com.uboxol.cloud.pandorasBox.db.repository.zcg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.CleanRecord;

@Repository
public interface CleanRecordRepository extends JpaRepository<CleanRecord, Long> {

}