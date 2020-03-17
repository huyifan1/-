package com.uboxol.cloud.pandorasBox.api.res;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 *	清货返回接口
 * @author huyifan
 */
@Getter
@Setter
@ToString
public class ClearResult {
	@ApiModelProperty(value = "请求结果",example = "200成功")
	String code;
	
	@ApiModelProperty(value = "请求结果描述",example = "成功/失败")
	String msg;
	
	@ApiModelProperty(value = "返回订单信息")
	List<OrderInformation> list;
	
}
