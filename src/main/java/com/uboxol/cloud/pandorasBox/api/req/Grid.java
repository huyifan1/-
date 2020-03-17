package com.uboxol.cloud.pandorasBox.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Grid {
	
	@ApiModelProperty(value = "业务方id(1饿了么 2友吧客)",example = "1")
    String bussinessId;
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "格子id",example = "A12")
	String gridId;
	
//	@ApiModelProperty(value = "动作",example = "1确认放餐 2确认取餐 3再次开格 4清货开格")
//	int action;
	
}
