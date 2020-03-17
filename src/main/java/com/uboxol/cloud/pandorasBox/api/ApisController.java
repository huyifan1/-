package com.uboxol.cloud.pandorasBox.api;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.uboxol.cloud.pandorasBox.api.req.CabinetQuery;
import com.uboxol.cloud.pandorasBox.api.req.ClearGrids;
import com.uboxol.cloud.pandorasBox.api.req.Grid;
import com.uboxol.cloud.pandorasBox.api.req.OperateQuery;
import com.uboxol.cloud.pandorasBox.api.req.RiderPutMeal;
import com.uboxol.cloud.pandorasBox.api.res.CabinetQueryBack;
import com.uboxol.cloud.pandorasBox.api.res.ClearResult;
import com.uboxol.cloud.pandorasBox.api.res.EntryResult;
import com.uboxol.cloud.pandorasBox.api.res.OperateQueryResult;
import com.uboxol.cloud.pandorasBox.api.res.OrderInformation;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.service.zcg.*;
import com.uboxol.cloud.tengu.common.server.annotation.ApiAbility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * （1）柜子信息查询接口
 * （2）骑手放餐接口
 * （3）骑手确认放餐接口
 * （4）用户取餐接口
 * （5）用户二次开格接口
 * （6）客服开格接口
 * （7）清货接口
 *
 * @author huyifan
 * @sice 2020/02/18 13:04
 */
@Slf4j
@Api(value = "Access-Controller", tags = "暂存柜")
@RestController
@RequestMapping("api/pandorasBox")
public class ApisController {
    private final CabinetInformationService cabinetInformationService;
    private final RiderPutMealService riderPutMealService;
    private final RiderConfirmPutMealService riderConfirmPutMealService;
    private final UserTakeMealService userTakeMealService;
    private final RepeatOpenGridService userRepeatTakeMealService;
    private final CustomerOpenGridService customerOpenGridService;
    private final CleanGoodsService cleanGoodsService;
    private final ConfirmCleanGoodsService confirmCleanGoodsService;
    private final OperateQueryService operateQueryService;
    private final QueryFreeGridsService queryFreeGridsService;
    private final CancelRiderPutMealService cancelRiderPutMealService;

   public ApisController(final CabinetInformationService cabinetInformationService,final RiderPutMealService riderPutMealService,
	   final RiderConfirmPutMealService riderConfirmPutMealService,final UserTakeMealService userTakeMealService,
	   final RepeatOpenGridService userRepeatTakeMealService,final CustomerOpenGridService customerOpenGridService,
	   final CleanGoodsService cleanGoodsService,final ConfirmCleanGoodsService confirmCleanGoodsService,final OperateQueryService operateQueryService,
	   final QueryFreeGridsService queryFreeGridsService,final CancelRiderPutMealService cancelRiderPutMealService) {
	   this.cabinetInformationService = cabinetInformationService;
	   this.riderPutMealService = riderPutMealService;
	   this.riderConfirmPutMealService  = riderConfirmPutMealService;
	   this.userTakeMealService = userTakeMealService;
	   this.userRepeatTakeMealService = userRepeatTakeMealService;
	   this.customerOpenGridService = customerOpenGridService;
	   this.cleanGoodsService = cleanGoodsService;
	   this.confirmCleanGoodsService = confirmCleanGoodsService;
	   this.operateQueryService = operateQueryService;
	   this.queryFreeGridsService = queryFreeGridsService;
	   this.cancelRiderPutMealService = cancelRiderPutMealService;
    }

      @ApiAbility(permission = true)
	  @ApiOperation(value = "1.柜子信息查询接口", notes = "根据业务方id查询柜子的信息")
	  @PostMapping("/cabinet/query")
	  public List<CabinetQueryBack> cabinetInformationQuery(CabinetQuery req) {
		  List<CabinetQueryBack> list = null;
	      try {
	    	  list = cabinetInformationService.query(req);
	      }catch (Exception e) {
	    	  logger.error("柜子信息查询接口出错:{}", e.getMessage(), e);
	      }
	      return list;	  
	  }

      @ApiAbility(permission = true)
	  @ApiOperation(value = "2.骑手放餐接口", notes = "骑手放餐，订单生成")
	  @PostMapping("/rider/putMeal")
	  public EntryResult riderPutMeal(RiderPutMeal req) {
		  EntryResult entryResult = null;
	      try {
	    	  entryResult = riderPutMealService.putMeal(req);
	      } catch (Exception e) {
	    	  logger.error("骑手放餐接口出错:{}", e.getMessage(), e);
	      }
	      return entryResult;
	  }
		  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "3.骑手确认放餐接口", notes = "骑手放餐，订单确认")
	  @PostMapping("/rider/confirmPutMeal")
	  public EntryResult riderConfirmPutMeal(Grid req) {
		  EntryResult entryResult = null;
		  try {
			  entryResult = riderConfirmPutMealService.confirmPutMeal(req);
	      } catch (Exception e) {
	    	  logger.error("骑手确认放餐接口出错:{}", e.getMessage(), e);
	      }
	      return entryResult;
	  }
		  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "4.用户取餐接口", notes = "用户取餐，订单完成")
	  @PostMapping("/user/takeMeal")
	  public EntryResult userTakeMeal(Grid req) {
		  EntryResult entryResult = null;
	      try {
	    	  entryResult = userTakeMealService.takeMeal(req);
	      } catch (Exception e) {
	    	  logger.error("用户取餐接口出错:{}", e.getMessage(), e);
	      }
	      return entryResult;
	  }
	  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "5.二次开格接口", notes = "二次开格，订单完成")
	  @PostMapping("/repeatOpenGrid")
	  public EntryResult userRepeatTakeMeal(Grid req) {
		  EntryResult entryResult = null;
	      try {
	    	  entryResult = userRepeatTakeMealService.repeatTakeMeal(req);
	      } catch (Exception e) {
	    	  logger.error("二次开格接口出错:{}", e.getMessage(), e);
	      }
	      return entryResult;
	  }
		  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "6.客服开格接口", notes = "客服开格，订单完成")
	  @PostMapping("/customer/openGrid")
	  public OrderInformation openGrid(Grid req) {
		  OrderInformation orderInformation = null;
	      try {
	    	  orderInformation = customerOpenGridService.openGrid(req);
	      } catch (Exception e) {
	    	  logger.error("客服开格接口出错:{}", e.getMessage(), e);
	      }
	      return orderInformation;
	  }
	
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "7.清货通知接口", notes = "清货通知，订单关闭")
	  @PostMapping("/cleanGoods")
	  public ClearResult cleanGoods(ClearGrids req) {
		  ClearResult clearResult = null;
	      try {
	    	  clearResult = cleanGoodsService.cleanGoods(req);
	      } catch (Exception e) {
	    	  logger.error("清货接口出错:{}", e.getMessage(), e);
	      }
	      return clearResult;
	  }
	  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "8.运营工具，查询可清货的格子", notes = "条件为：字段「清货通知」的值为：待清货")
	  @PostMapping("/operateQuery")
	  public OperateQueryResult operateQuery(OperateQuery req) {
		  OperateQueryResult clearResult = null;
	      try {
	    	  clearResult = operateQueryService.operate(req);
	      } catch (Exception e) {
	    	  logger.error("清货接口出错:{}", e.getMessage(), e);
	      }
	      return clearResult;
	  }
		  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "9.保洁/清货确认接口", notes = "清货确认，订单关闭")
	  @PostMapping("/confirmCleanGoods")
	  public ClearResult confirmCleanGoods(ClearGrids req) {
		  ClearResult clearResult = null;
	      try {
	    	  clearResult = confirmCleanGoodsService.confirmCleaning(req);
	      } catch (Exception e) {
	    	  logger.error("保洁/清货确认接口出错:{}", e.getMessage(), e);
	      }
	      return clearResult;
	  }
	  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "10.保洁清货查询(只需输入柜子ID,返回该柜子下所有空闲/超时格子)", notes = "查询柜子下的所有空闲格子")
	  @PostMapping("/queryFreeGrids")
	  public List<Counter> queryFreeGrid(ClearGrids req) {
		  List<Counter> list = null;
	      try {
	    	  list = queryFreeGridsService.query(req);
	      } catch (Exception e) {
	    	  logger.error("查询某个柜子下所以空闲格子接口出错:{}", e.getMessage(), e);
	      }
	      return list;
	  }
	  
	  @ApiAbility(permission = true)
	  @ApiOperation(value = "11.骑手取消放餐接口", notes = "取消放餐")
	  @PostMapping("/rider/cancelPutMeal")
	  public EntryResult cancelPutMeal(Grid req) {
		  EntryResult entryResult = null;
	      try {
	    	  entryResult = cancelRiderPutMealService.cancel(req);
	      } catch (Exception e) {
	    	  logger.error("骑手取消放餐接口出错:{}", e.getMessage(), e);
	      }
	      return entryResult;
	  }


    
}
