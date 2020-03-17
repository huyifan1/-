package com.uboxol.cloud.pandorasBox.api.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 1.柜子信息查询接口
 *
 * @author huyifan
 * @since 2019/10/28 15:34
 */
@Getter
@Setter
@ToString
public class CabinetQueryBack {
	
	//输出：对应的柜子id（机器编号），点位地址，格子数量，格子规格（大中小），格子id和状态等等
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "点位地址",example = "深圳")
	String pointAddr;
	
	@ApiModelProperty(value = "格子数量",example = "23")
	long num;
	
	@ApiModelProperty(value = "格子规格(大中小)",example = "大")
	String specific;
	
	@ApiModelProperty(value = "格子id",example = "A01")
	String gridId;
	
	@ApiModelProperty(value = "格子状态",example = "1")
	int gridStatus;
}
