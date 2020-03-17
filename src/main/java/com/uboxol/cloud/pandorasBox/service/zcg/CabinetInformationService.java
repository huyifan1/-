package com.uboxol.cloud.pandorasBox.service.zcg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uboxol.cloud.pandorasBox.api.req.CabinetQuery;
import com.uboxol.cloud.pandorasBox.api.req.ClearGrids;
import com.uboxol.cloud.pandorasBox.api.res.CabinetQueryBack;
import com.uboxol.cloud.pandorasBox.api.res.PointInformation;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CounterRepository;
import cn.ubox.cloud.node.NodeRemoteService;
import cn.ubox.cloud.node.VmRemoteService;
import cn.ubox.cloud.node.data.NodeNameAddress;
import cn.ubox.cloud.node.data.NodeWithOrg;
import lombok.extern.slf4j.Slf4j;
/*
 * 输入：所有/单个(柜子+(格子id)1,id2...)
 * 输出：对应的柜子id（机器编号），点位地址，格子数量，格子规格（大中小），格子id和状态等等
 */
@Slf4j
@Service
public class CabinetInformationService extends HttpProvider {
	
	private final CounterRepository counterRepository;
	private final NodeRemoteService.Iface nodeApiService;
	private final VmRemoteService.Iface vmRemoteService;
	
	private static final String PATH = "http://boxcloud.uboxol.com/api/list_cubes?";
	private String v = "1.0";
    private String t = "" + System.currentTimeMillis() / 1000;
    private String app_id = "ubox_20150820";
    private String app_secret = "PzUqPozMduUc6RohPxVdPmtXNfzzL6";
    
	
	 @Autowired 
	 public CabinetInformationService(final CounterRepository counterRepository, final NodeRemoteService.Iface nodeApiService,final VmRemoteService.Iface vmRemoteService) {
			 this.counterRepository = counterRepository;
			 this.nodeApiService = nodeApiService;
			 this.vmRemoteService = vmRemoteService;
	 }

	//柜子信息查询接口
	public List<CabinetQueryBack> query(CabinetQuery req) { 
		List<CabinetQueryBack> list = new ArrayList<CabinetQueryBack>();
		//查询接口去查数据库，没有再调用接口 boxcloud
		try {
			List<ClearGrids> cgList = req.getCabinetGrids();
			if(cgList!=null && cgList.size()>0) {
				for(ClearGrids cg : cgList) {
					String cabinetId = cg.getCabinetId();//一个柜子
					List<String> gridIds = cg.getGridIds();//多个格子
					List<Counter> clist =  counterRepository.findByCabinetId(cabinetId);
					if(clist.size()==0) {
						logger.info("数据库中没有当前柜子信息，初始化入库中---------------");
						list = getCabinetInf(cabinetId, gridIds);
					}else {
						if(gridIds==null || gridIds.size()==0) {
							List<Counter> allList = counterRepository.findByCabinetId(cabinetId);
							for(Counter c : allList) {
								CabinetQueryBack cqb = new CabinetQueryBack();
								cqb.setCabinetId(cabinetId);
								cqb.setPointAddr(c.getPointAddr());
								cqb.setNum(allList.size());
								cqb.setSpecific(c.getSpecs());
								cqb.setGridId(c.getGridId());
								cqb.setGridStatus(c.getGridCurStatus());
								list.add(cqb);
							}
						}else {
							for(String gridId : gridIds) {
								Counter c = counterRepository.findByCabinetIdAndGridId(cabinetId, gridId);
								if(c==null) {
									logger.info("有当前柜子，没有当前柜子的格子信息，查看是有该格子");
									//新入一条？？
								}else {
									logger.info("数据库中有当前柜子格子信息");
									CabinetQueryBack cqb = new CabinetQueryBack();
									cqb.setCabinetId(cabinetId);
									cqb.setPointAddr(c.getPointAddr());
									cqb.setNum(gridIds.size());
									cqb.setSpecific(c.getSpecs());
									cqb.setGridId(gridId);
									cqb.setGridStatus(c.getGridCurStatus());
									list.add(cqb);
								}
							}
						}
					}
				}
			}else {
				logger.info("传入参数为空");
			}
		}catch (Exception e) {
			logger.error("柜子信息查询出错:{}", e.getMessage(), e); 
		}
		return list;
	}
	
	public List<CabinetQueryBack> getCabinetInf(String cabinetId,List<String> gridIds){
		List<CabinetQueryBack> list = new ArrayList<CabinetQueryBack>();//返回结果信息
		List<Counter> counterList = new ArrayList<Counter>();//批量入库格子信息
		try {
			PointInformation p = getPointInf(cabinetId);//点位信息查询
			
			Map postmap = new HashMap();
	        HttpPost httpPost = new HttpPost(PATH + "app_id=" + app_id + "&v=" + v + "&t=" + t);
	        postmap.clear();
	        postmap.put("inner_code", cabinetId);
	        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
	        HttpResponse response = httpClient.execute(httpPost);
	        String content = consumeResponse(response.getEntity());
	        if(content.equals("illegal request param")) {
	        	 logger.info("检查机器号，illegal request param");
	        }else {
	        	JSONArray jsonarray = JSONArray.parseArray(content);
	        	for(int ja=0;ja<jsonarray.size();ja++) {
	        		JSONObject jo =  (JSONObject) jsonarray.get(ja);
	        		 int count = (int) jo.get("count");
	        		JSONArray strr = (JSONArray) jo.get("boxes");
	        		for(int i=0;i<strr.size();i++) {
			        	JSONObject j = (JSONObject) strr.get(i);
			        	String grid = (String) j.get("boxCode");
			        	Counter c =new Counter();
		        		c.setCabinetId(cabinetId);
						c.setGridId(grid);
						c.setSpecs("中");
						c.setGridCurStatus(3);
						c.setCleanStatus(0);
						c.setOrderCurStatus(0);
						c.setBranchCompany(p.getBranchCompany());
						c.setPointId(String.valueOf(p.getNodeId()));
						c.setPointName(p.getName());
						c.setPointAddr(p.getAddress());
						counterList.add(c);
						
						if(gridIds==null || gridIds.size()==0) {
							CabinetQueryBack cqb = new CabinetQueryBack();
							cqb.setCabinetId(cabinetId);
							cqb.setPointAddr(c.getPointAddr());
							cqb.setNum(count);
							cqb.setSpecific(c.getSpecs());
							cqb.setGridId(c.getGridId());
							cqb.setGridStatus(c.getGridCurStatus());
							list.add(cqb);
						}else {
							if(gridIds.contains(grid)) {
								CabinetQueryBack cqb = new CabinetQueryBack();
								cqb.setCabinetId(cabinetId);
								cqb.setPointAddr(c.getPointAddr());
								cqb.setNum(gridIds.size());
								cqb.setSpecific(c.getSpecs());
								cqb.setGridId(c.getGridId());
								cqb.setGridStatus(c.getGridCurStatus());
								list.add(cqb);
							}
						}	
		        	}
		        }
	        	counterRepository.saveAll(counterList);
		        logger.info("批量格子入库");
        	}
		}catch (Exception e) {
			logger.error("柜子格子信息查询出错:{}", e.getMessage(), e); 
		}
		return list;
	}
	
	public List<Counter> update() {
		List<Counter> counterList = new ArrayList<Counter>();//批量入库格子信息
		 try {
			 Set<String> slist = new HashSet<String>();//当前数据库中的所有柜子
			 List<Counter> list =  counterRepository.findAll();
			 for(Counter c : list) {
				 slist.add(c.getCabinetId());
			 }
			 for(String cabinetId : slist) {
			 	PointInformation p = getPointInf(cabinetId);//点位信息查询
				Map postmap = new HashMap();
		        HttpPost httpPost = new HttpPost(PATH + "app_id=" + app_id + "&v=" + v + "&t=" + t);
		        postmap.clear();
		        postmap.put("inner_code", cabinetId);
		        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
		        HttpResponse response = httpClient.execute(httpPost);
		        String content = consumeResponse(response.getEntity());
		        if(content.equals("illegal request param")) {
		        	 logger.info("检查机器号，illegal request param");
		        }else {
		        	JSONArray jsonarray = JSONArray.parseArray(content);
		        	for(int ja=0;ja<jsonarray.size();ja++) {
		        		JSONObject jo =  (JSONObject) jsonarray.get(ja);
		        		JSONArray strr = (JSONArray) jo.get("boxes");
		        		for(int i=0;i<strr.size();i++) {
				        	JSONObject j = (JSONObject) strr.get(i);
				        	String grid = (String) j.get("boxCode");
				        	Counter c = counterRepository.findByCabinetIdAndGridId(cabinetId, grid);
				        	if(c == null) { 
				        		c =new Counter();
				        		c.setCabinetId(cabinetId);
								c.setGridId(grid);
								c.setSpecs("中");
								c.setGridCurStatus(3);
								c.setCleanStatus(0);
								c.setOrderCurStatus(0);
								c.setBranchCompany(p.getBranchCompany());
								c.setPointId(String.valueOf(p.getNodeId()));
								c.setPointName(p.getName());
								c.setPointAddr(p.getAddress());
								counterList.add(c);
				        	}
				        }
		        	}
		        }
			 }
			 if(counterList.size()>0) {
     			counterRepository.saveAll(counterList);
			        logger.info("批量格子入库");
     		}
		 }catch (Exception e) {
				logger.error("添加新格子信息入库出错:{}", e.getMessage(), e); 
		 }
		return counterList;
	 }
	
	
	
	
	public PointInformation getPointInf(String cabinetId) {
		PointInformation p= new PointInformation();
		try {
			List<String> cabinetIds = new ArrayList<String>();
			cabinetIds.add(cabinetId);
			Map<String, NodeNameAddress> map = vmRemoteService.listNodeByInnerCode(cabinetIds);
			logger.info("--------vmRemoteService---------"+map.toString());
			if(!map.isEmpty()) {
				NodeNameAddress nna = map.get(cabinetIds.get(0));
				logger.info("--------map.get(cabinetIds.get(0))----cabinetIds.get(0)-"+cabinetIds.get(0)+"----"+nna);
				if(!StringUtils.isEmpty(nna)) {
					p.setName(nna.getName()==null?"":nna.getName());
					logger.info("--------nna.getName()--------"+nna.getName());
					long nodeId = nna.getNodeId();
					p.setNodeId(nodeId);
					logger.info("--------nna.getNodeId()--------"+nodeId);
					
					List<Long> nodeIds = new ArrayList<Long>();
					nodeIds.add(nodeId);
					Map<Long, NodeNameAddress> maps = nodeApiService.getNodeNameAddress(nodeIds);
					if(!maps.isEmpty()) {
						NodeNameAddress nnas =maps.get(nodeId);
						if(!StringUtils.isEmpty(nnas)) {
							p.setAddress(nnas.getAddress()==null?"":nnas.getAddress());
							logger.info("--------nnas.getAddress()--------"+nnas.getAddress());
						}
					}
					
					if(nodeId>0L) {
						NodeWithOrg nodeWithOrg = nodeApiService.getNodeWithOrgById(nodeId);
						if(nodeWithOrg != null && nodeWithOrg.getOrgs()!=null) {
							String branchCompany = nodeWithOrg.getOrgs().getOrg4Name();
			            	p.setBranchCompany(branchCompany);
			            }
					}
					logger.info("通过vmRemoteService服务查找机器号"+cabinetIds.get(0)+"的address"+p.getAddress()+"name"+p.getName()+"nodeId"+p.getNodeId());
				}
			}
		}catch (Exception e) {
			logger.error("点位信息查询出错:{}", e.getMessage(), e); 
		}
		return p;
	}
	
	
	
	
}
