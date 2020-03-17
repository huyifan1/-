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
 * 类说明    订单信息录入表
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="order")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Order{
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@ApiModelProperty(value = "订单id",example = "...")
	String orderId;
	
	@ApiModelProperty(value = "业务方id（渠道方）",example = "1饿了么 2友吧客")
    String bussinessId;
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "格子id",example = "59733314")
	String gridId;
	
	@ApiModelProperty(value = "格子规格(大中小)",example = "大")
	String specs;
	
	@ApiModelProperty(value = "分公司",example = "深圳")
	String branchCompany;
	
	@ApiModelProperty(value = "点位id",example = "59733314")
	String pointId;
	
	@ApiModelProperty(value = "点位名称",example = "深圳xx路")
	String pointName;
	
	@ApiModelProperty(value = "订单原因",example = "骑手送货")
	String orderReason;
	
	@ApiModelProperty(value = "格子状态",example = "1交互中 2已占用 3空闲中 4超时")
	int gridStatus;
	
	@ApiModelProperty(value = "订单状态",example = "1订单生成 2订单确认 3订单完成 4订单关闭 5订单取消")
	int orderStatus;
	
	@ApiModelProperty(value = "清货状态",example = "1待清货 2已清货")
	int cleanStatus;
	
	@ApiModelProperty(value = "订单生成时间（骑手放餐）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderTime;
	
	@ApiModelProperty(value = "订单确认时间（骑手确认放餐）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderConfirmTime;
	
	@ApiModelProperty(value = "订单完成时间（用户确认取餐）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderCompleteTime;

	@ApiModelProperty(value = "订单关闭时间（运营清货）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderClosedTime;
	
	@ApiModelProperty(value = "订单取消时间（骑手取消放餐）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderCancelTime;
	
}
