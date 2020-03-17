package com.uboxol.cloud.pandorasBox.api.res;

import java.util.List;

import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;

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
public class OperateQueryResult {
	@ApiModelProperty(value = "请求结果",example = "200成功")
	String code;
	
	@ApiModelProperty(value = "请求结果描述",example = "成功/失败")
	String msg;
	
	@ApiModelProperty(value = "返回盒子信息")
	List<Counter> list;
	
}
