package com.uboxol.cloud.pandorasBox.db.entity.zcg;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author huyifan
 * @version 创建时间：2019年4月8日 下午6:06:43
 * <p>
 * 类说明    柜子信息录入表
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="clean_record")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class CleanRecord{
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "格子id",example = "59733314")
	String gridId;
	
	@ApiModelProperty(value = "当前订单id",example = "...")
	String curOrderId;
	
	@ApiModelProperty(value = "对应时间",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp cleanTime;
	
}
