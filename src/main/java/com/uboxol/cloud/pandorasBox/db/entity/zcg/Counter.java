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
@Table(name="counter")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Counter{
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@ApiModelProperty(value = "业务方id（渠道方:1饿了么 2友吧客）",example = "1")
    String bussinessId;
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "格子id",example = "59733314")
	String gridId;
	
	@ApiModelProperty(value = "格子规格(大中小)",example = "大")
	String specs;
	
	@ApiModelProperty(value = "格子当前状态(1交互中 2已占用 3空闲中 4超时)",example = "1")
	int gridCurStatus;
	
	@ApiModelProperty(value = "分公司",example = "深圳")
	String branchCompany;
	
	@ApiModelProperty(value = "点位id",example = "59733314")
	String pointId;
	
	@ApiModelProperty(value = "点位名称",example = "深圳xx路")
	String pointName;
	
	@ApiModelProperty(value = "点位地址",example = "深圳xx路")
	String pointAddr;
	
	@ApiModelProperty(value = "当前订单id",example = "...")
	String curOrderId;
	
	@ApiModelProperty(value = "订单当前状态(1订单生成 2订单确认 3订单完成 4订单关闭)",example = "1")
	int orderCurStatus;
	
	@ApiModelProperty(value = "对应时间",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp curTime;
	
	@ApiModelProperty(value = "清货状态(1待清货 2已清货)",example = "1")
	int cleanStatus;
	
	@ApiModelProperty(value = "清货时间（运营清货）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp cleanTime;
	
}
