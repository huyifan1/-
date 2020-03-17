package com.uboxol.cloud.pandorasBox.api.req;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClearGrids {
	
	@ApiModelProperty(value = "柜子id",example = "59733314")
	String cabinetId;
	
	@ApiModelProperty(value = "批量格子id",example = "A12")
	List<String> gridIds;
	
}
