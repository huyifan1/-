package com.uboxol.cloud.pandorasBox.api.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author huyifan
 */
@Getter
@Setter
@ToString
public class PointInformation {
	
	@ApiModelProperty(value = "点位地址")
	String address;
	
	@ApiModelProperty(value = "点位名称")
	String name;
	
	@ApiModelProperty(value = "分公司")
	String branchCompany;
	
	@ApiModelProperty(value = "2L")
	 long nodeId;
	
}
