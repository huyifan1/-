package com.uboxol.cloud.pandorasBox.api.req;

import java.util.List;

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
public class CabinetQuery {
	
	/*
	 * 输入：所有/单个(柜子id +格子id )
	 * 输出：对应的柜子id（机器编号），点位地址，格子数量，格子规格（大中小），格子id和状态等等
	 */
	@ApiModelProperty(value = "批量(柜子id+(格子id1,id2..))",example = "59733314+A01")
	List<ClearGrids> cabinetGrids; 
	
}
