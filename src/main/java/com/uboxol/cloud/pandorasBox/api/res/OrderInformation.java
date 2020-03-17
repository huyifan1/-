package com.uboxol.cloud.pandorasBox.api.res;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 返回订单信息
 *
 * @author huyifan
 * @since 2019/10/28 15:34
 */
@Getter
@Setter
@ToString
public class OrderInformation {
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	@ApiModelProperty(value = "订单id",example = "...")
	String orderId;
	
	@ApiModelProperty(value = "业务方id(1饿了么 2友吧客)",example = "1")
    String bussinessId;
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "格子id",example = "A01")
	String gridId;
	
	@ApiModelProperty(value = "格子状态",example = "1")
	int gridStatus;
	
	@ApiModelProperty(value = "订单状态",example = "1")
	int orderStatus;
	
	@ApiModelProperty(value = "订单生成时间",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderTime;
	
	@ApiModelProperty(value = "订单确认时间",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderConfirmTime;
	
	@ApiModelProperty(value = "订单完成时间",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderCompleteTime;
	
	@ApiModelProperty(value = "订单关闭时间（运营清货）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderClosedTime;
	
	@ApiModelProperty(value = "订单取消时间（骑手取消放餐）",example = "2019-11-07 09:15:55")
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	Timestamp orderCancelTime;

}
