/*
 * package com.uboxol.cloud.mermaid.service;
 * 
 * import com.uboxol.cloud.mermaid.BaseSpringBootTest; import
 * com.uboxol.cloud.mermaid.api.req.CustomerComplaintQuery; import
 * com.uboxol.cloud.mermaid.api.req.Query; import
 * com.uboxol.cloud.mermaid.api.req.SubOrder; import
 * com.uboxol.cloud.mermaid.api.res.QueryBack; import
 * com.uboxol.cloud.mermaid.api.res.SaveComplaint; import
 * com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint; import
 * com.uboxol.cloud.mermaid.service.kefu.CustomerComplaintService; import
 * lombok.extern.slf4j.Slf4j;
 * 
 * import java.sql.Timestamp; import java.util.Date; import java.util.List;
 * 
 * import org.junit.Assert; import org.junit.Test; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.data.domain.Page;
 * 
 *//**
	 * model: mermaid
	 *
	 * @author liyunde
	 * @since 2019/10/31 15:54
	 *//*
		 * @Slf4j public class CustomerComplaintServiceTest extends BaseSpringBootTest {
		 * 
		 * @Autowired CustomerComplaintService service;
		 * 
		 * //测试条件查询
		 * 
		 * @Test public void testSearch() {
		 * 
		 * CustomerComplaintQuery query = new CustomerComplaintQuery();
		 * 
		 * query.setTransactionId("visionpay8BC1D65D671807EAE4840706");
		 * 
		 * Page<CustomerComplaint> search = service.search(query);
		 * 
		 * logger.info("search result:{}", search);
		 * 
		 * Assert.assertTrue(search.getTotalElements() >= 0); }
		 * 
		 * @Test public void testQuery() { Query request = new Query();
		 * 
		 * request.setTransactionId("visionpay8BC1D65D671807EAE4840706"); //测试查询
		 * QueryBack queryBack = service.query(request); logger.info("search result:{}",
		 * queryBack);
		 * 
		 * //测试保存 List<SubOrder> bOrders = queryBack.getSubOrderList(); SubOrder bOrder
		 * = bOrders.get(0); bOrder.setContact("13715073248");
		 * bOrder.setCcPhenomenon("1"); bOrder.setHandleStatus(2);
		 * bOrder.setHandleWay("1"); bOrder.setQuestionClassify("1");
		 * bOrder.setVisitStatus(1); bOrder.setVisitAdvice("1");
		 * bOrder.setVisitor("测试人"); bOrder.setCcStatus(1);bOrder.setCcTime(new
		 * Timestamp(new Date().getTime()));bOrder.setCcChannel("客服后台");
		 * bOrder.setVisitRemark("回访测试"); bOrder.setVisitTime(new Timestamp(new
		 * Date().getTime())); SaveComplaint saveComplaint = service.save(bOrder);
		 * logger.info("save result:{}", saveComplaint);
		 * 
		 * //测试提交数据 queryBack.setSubOrderList(bOrders); service.commit(queryBack);
		 * logger.info("commit result:{}", queryBack);
		 * 
		 * }
		 * 
		 * 
		 * 
		 * }
		 */