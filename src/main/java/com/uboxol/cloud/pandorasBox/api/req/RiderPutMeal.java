package com.uboxol.cloud.pandorasBox.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 2.骑手放餐接口
 *
 * @author huyifan
 * @since 2019/10/28 15:34
 */
@Getter
@Setter
@ToString
public class RiderPutMeal {
	//输入：业务渠道id，格子id
    //输入：业务渠道id，柜子id且格子类型
	@ApiModelProperty(value = "业务方id(1饿了么 2友吧客)",example = "1")
    String bussinessId;
	
	@ApiModelProperty(value = "格子id",example = "A01")
	String gridId;
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "格子规格(大中小)",example = "大")
	String specific;
	
    
    
}
