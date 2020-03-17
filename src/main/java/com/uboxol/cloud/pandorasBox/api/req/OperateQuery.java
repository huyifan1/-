package com.uboxol.cloud.pandorasBox.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 *  运营工具，查询可清货的格子
 *
 * @author huyifan
 * @since 2019/10/28 15:34
 */
@Getter
@Setter
@ToString
public class OperateQuery {
	
	/*
	 * 运营工具，查询可清货的格子，条件为：字段「清货通知」的值为：待清货
	 * */
	
	@ApiModelProperty(value = "清货状态(1待清货 2已清货)",example = "1")
	int cleanStatus;
	
}
