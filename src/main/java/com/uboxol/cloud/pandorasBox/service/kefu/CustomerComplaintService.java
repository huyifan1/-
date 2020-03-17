/*
 * package com.uboxol.cloud.mermaid.service.kefu; import
 * cn.ubox.cloud.node.NodeRemoteService; import
 * cn.ubox.cloud.node.data.NodeWithOrg; import
 * io.netty.util.internal.StringUtil;
 * 
 * import com.alibaba.druid.util.StringUtils; import
 * com.uboxol.cloud.mermaid.api.req.CustomerComplaintQuery; import
 * com.uboxol.cloud.mermaid.api.req.Query; import
 * com.uboxol.cloud.mermaid.api.req.SubOrder; import
 * com.uboxol.cloud.mermaid.api.req.SubOrderAll; import
 * com.uboxol.cloud.mermaid.api.res.BasicInformation; import
 * com.uboxol.cloud.mermaid.api.res.QueryBack; import
 * com.uboxol.cloud.mermaid.api.res.SaveComplaint; import
 * com.uboxol.cloud.mermaid.db.entity.bx.AlipayNotifyImage; import
 * com.uboxol.cloud.mermaid.db.entity.bx.AlipayOrder; import
 * com.uboxol.cloud.mermaid.db.entity.kefu.BOrder; import
 * com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint; import
 * com.uboxol.cloud.mermaid.db.entity.kefu.Image; import
 * com.uboxol.cloud.mermaid.db.entity.kefu.SaveBOrder; import
 * com.uboxol.cloud.mermaid.db.mapper.CustomerComplaintMapper; import
 * com.uboxol.cloud.mermaid.db.repository.kefu.BOrderRepository; import
 * com.uboxol.cloud.mermaid.db.repository.kefu.CustomerComplaintRepository;
 * import com.uboxol.cloud.mermaid.db.repository.kefu.ImageRepository; import
 * com.uboxol.cloud.mermaid.db.repository.kefu.SaveBOrderRepository; import
 * com.uboxol.cloud.mermaid.service.bx.AlipayOrderService; import
 * com.uboxol.cloud.mermaid.utils.HyfUtils; import
 * com.uboxol.cloud.mermaid.utils.MailUtils; import
 * com.uboxol.cloud.mermaid.utils.TimeUtils; import
 * com.uboxol.gen.api.Cds2APIService; import com.uboxol.gen.api.Order; import
 * com.uboxol.gen.api.OrderStatus; import com.uboxol.gen.api.Response;
 * 
 * import lombok.extern.slf4j.Slf4j; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.data.domain.Page; import
 * org.springframework.data.domain.PageImpl; import
 * org.springframework.data.domain.PageRequest; import
 * org.springframework.stereotype.Service;
 * 
 * import java.io.UnsupportedEncodingException; import java.math.BigDecimal;
 * import java.sql.Timestamp; import java.text.SimpleDateFormat; import
 * java.util.ArrayList; import java.util.Collections; import java.util.Date;
 * import java.util.List; import java.util.stream.Collectors;
 * 
 * import javax.mail.MessagingException; import
 * javax.mail.internet.AddressException;
 * 
 * @Slf4j
 * 
 * @Service public class CustomerComplaintService {
 * 
 * private final Cds2APIService.Iface apiService; private final
 * NodeRemoteService.Iface nodeApiService; private final AlipayOrderService
 * alipayOrderService; private final KesuQueryService kesuQueryService; private
 * final CustomerComplaintRepository customerComplaintRepository; private final
 * BOrderRepository bOrderRepository; private final SaveBOrderRepository
 * saveBOrderRepository; private final ImageRepository imageRepository; private
 * final CustomerComplaintMapper customerComplaintMapper;
 * 
 * private static final String PATH =
 * "https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid="; private final
 * SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
 * 
 * @Autowired public CustomerComplaintService(final Cds2APIService.Iface
 * apiService, final NodeRemoteService.Iface nodeApiService,final
 * AlipayOrderService alipayOrderService, final KesuQueryService
 * kesuQueryService,final CustomerComplaintRepository
 * customerComplaintRepository, final BOrderRepository bOrderRepository,final
 * ImageRepository imageRepository, final SaveBOrderRepository
 * saveBOrderRepository,final CustomerComplaintMapper customerComplaintMapper) {
 * this.apiService = apiService; this.nodeApiService = nodeApiService;
 * this.alipayOrderService = alipayOrderService; this.kesuQueryService =
 * kesuQueryService; this.customerComplaintRepository =
 * customerComplaintRepository; this.bOrderRepository = bOrderRepository;
 * this.imageRepository = imageRepository; this.saveBOrderRepository =
 * saveBOrderRepository; this.customerComplaintMapper = customerComplaintMapper;
 * }
 * 
 * 
 *//**
	 * 客诉信息查询
	 *
	 * @return CustomerComplaint
	 */
/*
 * public QueryBack query(Query req) { QueryBack queryBack = null;
 * BasicInformation basicInformation = new BasicInformation(); List<String> uids
 * = new ArrayList<String>(); BigDecimal amount = BigDecimal.ZERO;
 * List<SubOrder> subOrderList = new ArrayList<SubOrder>(); String[]
 * imageBefore; String[] imageAfter; try {
 * //先去自己数据库里查看是否已经录入过客诉了，录入过直接返回信息，没录入过再查 queryBack =
 * kesuQueryService.findAll(req); if(queryBack == null) { queryBack = new
 * QueryBack(); List<AlipayOrder> alipayOrderlist =
 * alipayOrderService.findAlipayOrder(req);
 * System.out.println(alipayOrderlist.size());
 * logger.info("查询数据库里"+alipayOrderlist.size()+"");
 * if(alipayOrderlist.size()==0) throw new
 * RuntimeException("alipay_order表中没有对应的订单"); //存基础信息 String transactionId =
 * alipayOrderlist.get(0).getTransactionId();
 * basicInformation.setTransactionId(transactionId);//transactionID
 * basicInformation.setTradeNo(alipayOrderlist.get(0).getTradeNo());//支付宝交易单号
 * basicInformation.setVmId(alipayOrderlist.get(0).getVmId());//机器编号(
 * b_order表里也有)
 * basicInformation.setCreateTime(alipayOrderlist.get(0).getCreateTime());//发生时间
 * 
 * if(!StringUtil.isNullOrEmpty(alipayOrderService.findByTransactionId(
 * transactionId))) {
 * uids.add(alipayOrderService.findByTransactionId(transactionId)); }
 * 
 * List<Long> collect =
 * alipayOrderlist.stream().map(AlipayOrder::getOrderId).collect(Collectors.
 * toList()); List<BigDecimal> costs =
 * alipayOrderlist.stream().map(AlipayOrder::getCost).collect(Collectors.toList(
 * )); for (BigDecimal cost : costs) { amount = amount.add(cost); }
 * basicInformation.setAmount(amount);//订单金额
 * 
 * List<Order> orderList = apiService.getOrderListById(collect); if
 * (orderList.size()==0) throw new RuntimeException("b_order表中没有对应的订单"); for
 * (Order order : orderList) { if(order.getUid()>=0) {
 * uids.add(order.getUid()+""); } }
 * 
 * basicInformation.setUserUID(HyfUtils.removeDuplicate(uids));//用户UID // Date d
 * = sdf.parse(StringUtil.isNullOrEmpty(orderList.get(0).getCompletedTime())?new
 * Date().toString():orderList.get(0).getCompletedTime()); //
 * basicInformation.setDeductionTime(new Timestamp(d.getTime()));
 * basicInformation.setDeductionTime(new Timestamp(new Date().getTime()));
 * 
 * //basicInformation错误码(dw库)
 * logger.info("orderList.get(0).getNodeId()"+orderList.get(0).getNodeId());
 * NodeWithOrg nodeWithOrg=null; try { if(orderList.get(0).getNodeId()>=0L) {
 * nodeWithOrg =
 * nodeApiService.getNodeWithOrgById(orderList.get(0).getNodeId()); } } catch
 * (Exception e) { logger.error("点位获取分公司"+e.getMessage(),e); } if(nodeWithOrg ==
 * null || nodeWithOrg.getOrgs()==null) {
 * basicInformation.setBrachCompany("");//分公司 }else {
 * basicInformation.setBrachCompany(nodeWithOrg.getOrgs().getOrg4Name());//分公司 }
 * 
 * logger.info("basicInformation"+basicInformation.toString()); //基础信息
 * queryBack.setTrade(basicInformation); //订单数
 * queryBack.setOrderNum(orderList.size());
 * 
 * //列表信息 for(int i=0;i<orderList.size();i++) { Order order = orderList.get(i);
 * SubOrder bOrder = new SubOrder(); bOrder.setOrderId(order.getOrderId());
 * //bOrder.setOrderTime(LocalDateTime.parse(order.getCreatedTime(),FORMAT));
 * bOrder.setOrderTime(new Timestamp(new Date().getTime()));
 * bOrder.setProductPrice(new BigDecimal(order.getCost()));
 * bOrder.setProductName(order.getProductFullName()); subOrderList.add(bOrder);
 * } queryBack.setSubOrderList(subOrderList);
 * 
 * //开柜前图片 List<AlipayNotifyImage> list =
 * alipayOrderService.findPictures(transactionId, "1"); imageBefore = new
 * String[list.size()]; for (int i = 0; i < list.size(); i++) {
 * AlipayNotifyImage alipayNotifyImage = list.get(i);
 * imageBefore[i]=PATH+alipayNotifyImage.getImageId(); }
 * queryBack.setImageBefore(imageBefore);
 * 
 * //开柜后图片 List<AlipayNotifyImage> list2 =
 * alipayOrderService.findPictures(transactionId, "2"); imageAfter = new
 * String[list2.size()]; for (int i = 0; i < list2.size(); i++) {
 * AlipayNotifyImage alipayNotifyImage = list2.get(i);
 * imageAfter[i]=PATH+alipayNotifyImage.getImageId(); }
 * queryBack.setImageAfter(imageAfter); }
 * 
 * } catch (Exception e) { logger.error("客诉信息查询出错:{}", e.getMessage(), e); }
 * 
 * return queryBack;
 * 
 * }
 * 
 * public SaveComplaint save(SubOrder dto) { SaveComplaint saveComplaint = new
 * SaveComplaint(); try { SaveBOrder bOrder =
 * saveBOrderRepository.findByOrderId(dto.getOrderId()); if(bOrder == null) {
 * bOrder = new SaveBOrder(); } bOrder.setTransactionId(dto.getTransactionId());
 * bOrder.setKsSubNo(SDF.format(new Date())+"-"+dto.getOrderId());
 * //可以拿transactionId跟子单表transactionId关联子单有几个，基础上加1
 * dto.setKsSubNo(SDF.format(new Date())+"-"+dto.getOrderId());
 * bOrder.setOrderId(dto.getOrderId()); bOrder.setOrderTime(dto.getOrderTime());
 * bOrder.setProductPrice(dto.getProductPrice());
 * bOrder.setProductName(dto.getProductName());
 * bOrder.setCcPhenomenon(dto.getCcPhenomenon());
 * bOrder.setCcAppeal(dto.getCcAppeal()); bOrder.setContact(dto.getContact());
 * bOrder.setVisitor(dto.getVisitor());
 * bOrder.setVisitStatus(dto.getVisitStatus());
 * if(bOrder.getVisitStatus()!=null) { bOrder.setVisitTime(new Timestamp(new
 * Date().getTime())); dto.setVisitTime(new Timestamp(new Date().getTime())); }
 * bOrder.setVisitAdvice(dto.getVisitAdvice());
 * bOrder.setVisitRemark(dto.getVisitRemark());
 * bOrder.setHandle(dto.getHandle());
 * 
 * bOrder.setHandleStatus(dto.getHandleStatus());//1 待处理 2 处理中 3 已处理
 * //子单的处理状态为：待处理 或 处理中 时，那么子单的客诉状态为：进行中 if(dto.getHandleStatus()==3) {
 * bOrder.setCcStatus(9); dto.setCcStatus(9); }else { bOrder.setCcStatus(1);
 * dto.setCcStatus(1); } bOrder.setCcTime(new Timestamp(new Date().getTime()));
 * bOrder.setCcChannel("客服后台"); bOrder.setHandleTime(new Timestamp(new
 * Date().getTime())); dto.setCcTime(new Timestamp(new Date().getTime()));
 * dto.setCcChannel("客服后台"); dto.setHandleTime(new Timestamp(new
 * Date().getTime())); bOrder.setHandleWay(dto.getHandleWay());
 * bOrder.setQuestionClassify(dto.getQuestionClassify());
 * bOrder.setHandleRemark(dto.getHandleRemark());
 * saveBOrderRepository.save(bOrder);
 * 
 * List<SaveBOrder> list =
 * saveBOrderRepository.findByTransactionId(dto.getTransactionId());
 * List<Integer> kesuList =
 * list.stream().map(SaveBOrder::getCcStatus).collect(Collectors.toList()); int
 * kesuNums = 0; for(int i = 0;i<kesuList.size();i++) {
 * if(kesuList.get(i)!=null) { kesuNums +=1; } }
 * saveComplaint.setKesuNum(kesuNums);
 * 
 * saveComplaint.setSubOrder(dto);
 * 
 * } catch (Exception e) { logger.error("客诉信息查询出错:{}", e.getMessage(), e); }
 * 
 * return saveComplaint; }
 * 
 * 
 * public QueryBack commit(QueryBack queryBack) { try { BasicInformation
 * basicInformation = queryBack.getTrade(); //客诉主编号基本信息存入表中 CustomerComplaint
 * customerComplaint =
 * customerComplaintRepository.findByTransactionId(basicInformation.
 * getTransactionId()); if(customerComplaint == null) { customerComplaint = new
 * CustomerComplaint(); } customerComplaint.setKsNo(SDF.format(new Date()));
 * customerComplaint.setTransactionId(basicInformation.getTransactionId());
 * customerComplaint.setEductionTime(basicInformation.getDeductionTime());
 * 
 * List<String> uids = HyfUtils.removeDuplicate(basicInformation.getUserUID());
 * for(int i=0;i<uids.size();i++) { if(i==uids.size()-1) {
 * customerComplaint.setUId2(uids.get(i)); }else {
 * customerComplaint.setUId1(uids.get(i)); } }
 * customerComplaint.setUids(HyfUtils.listToString(HyfUtils.removeDuplicate(
 * basicInformation.getUserUID())));
 * customerComplaint.setOrderNum(queryBack.getOrderNum());
 * customerComplaint.setTradeNo(basicInformation.getTradeNo());
 * customerComplaint.setEductionMoney(basicInformation.getAmount());
 * customerComplaint.setMachineNo(basicInformation.getVmId());
 * customerComplaint.setBranchCompany(basicInformation.getBrachCompany());
 * 
 * customerComplaintRepository.saveAndFlush(customerComplaint);
 * 
 * //客诉子编号子订单信息存入表中 List<SubOrder> list = queryBack.getSubOrderList(); if(list
 * !=null) { for(int i=0;i<list.size();i++) { SubOrder bOrder = list.get(i);
 * Long orderId = bOrder.getOrderId(); BOrder bOrder2 =
 * bOrderRepository.findByOrderId(orderId); if(bOrder2 == null) { bOrder2 = new
 * BOrder(); } bOrder2.setTransactionId(customerComplaint.getTransactionId());
 * bOrder2.setKsSubNo(bOrder.getKsSubNo());
 * bOrder2.setCcStatus(bOrder.getCcStatus());
 * bOrder2.setCcTime(bOrder.getCcTime());
 * bOrder2.setCcChannel(bOrder.getCcChannel()); bOrder2.setOrderId(orderId);
 * bOrder2.setOrderTime(bOrder.getOrderTime());
 * bOrder2.setProductPrice(bOrder.getProductPrice());
 * bOrder2.setProductName(bOrder.getProductName());
 * bOrder2.setCcPhenomenon(bOrder.getCcPhenomenon());
 * bOrder2.setCcAppeal(bOrder.getCcAppeal());
 * bOrder2.setContact(bOrder.getContact());
 * bOrder2.setVisitor(bOrder.getVisitor());
 * bOrder2.setVisitStatus(bOrder.getVisitStatus());
 * bOrder2.setVisitTime(bOrder.getVisitTime());
 * bOrder2.setVisitAdvice(bOrder.getVisitAdvice());
 * bOrder2.setVisitRemark(bOrder.getVisitRemark());
 * //bOrder2.setHandle(bOrder.getHandle());
 * bOrder2.setHandleStatus(bOrder.getHandleStatus());
 * bOrder2.setHandleTime(bOrder.getHandleTime());
 * 
 * String handleWay = bOrder.getHandleWay();
 * //如果处理方式有值，会有提交人，判断当前子单是否已经有处理人，没有就取当前处理人入库当前单，有处理人就不改变
 * if(!StringUtils.isEmpty(handleWay) &&
 * StringUtils.isEmpty(bOrder2.getHandle()) ) {
 * bOrder2.setHandle(bOrder.getHandle()); }
 * 
 * bOrder2.setHandleWay(handleWay); if(!StringUtils.isEmpty(handleWay) &&
 * handleWay.equals("退款")) { OrderStatus orderStatus = new OrderStatus();
 * orderStatus.setOrderId(orderId); orderStatus.setStatus(8); Order order =
 * apiService.getOrderDetailById(orderId); if (order==null) throw new
 * RuntimeException("cds获取订单详情表中没有对应的该订单"); int originaStatus =
 * order.getStatus(); orderStatus.setOriginaStatus(originaStatus); int reasonId
 * = 8501; if(originaStatus==5) { reasonId = 8*1000+originaStatus*100+1; }else
 * if(originaStatus==6) { reasonId = 8*1000+originaStatus*100+0; }else
 * if(originaStatus==7) { reasonId = 8*1000+originaStatus*100+4; }else
 * if(originaStatus==9) { reasonId = 8*1000+originaStatus*100+0; }
 * orderStatus.setReasonId(reasonId); orderStatus.setDealLog("等待退款");
 * orderStatus.setSysId(40); orderStatus.setVendoutType(0); Response response =
 * apiService.updateOrderStatus(orderStatus);
 * logger.info("退款结果为flag:"+response.getFlag()+"(1-成功 0-失败),Message"+response.
 * getMessage()); }
 * 
 * bOrder2.setQuestionClassify(bOrder.getQuestionClassify());
 * bOrder2.setHandleRemark(bOrder.getHandleRemark());
 * bOrderRepository.saveAndFlush(bOrder2); }
 * 
 * }
 * 
 * //trans——ID 对应的图片信息存入表中 String[] bImages = queryBack.getImageBefore();
 * String[] aImages = queryBack.getImageAfter();
 * 
 * Image image = null; for (int a = 0; a < bImages.length; a++) { image =
 * imageRepository.findByTransactionIdAndImageIdAndFlag(customerComplaint.
 * getTransactionId(), bImages[a], "1"); if(image==null) { image = new Image();
 * } image.setTransactionId(customerComplaint.getTransactionId());
 * image.setImageId(bImages[a]); image.setNum(a+1); image.setFlag("1");
 * imageRepository.saveAndFlush(image); } for (int a = 0; a < aImages.length;
 * a++) { image =
 * imageRepository.findByTransactionIdAndImageIdAndFlag(customerComplaint.
 * getTransactionId(), aImages[a], "2"); if(image==null) { image = new Image();
 * } image.setTransactionId(customerComplaint.getTransactionId());
 * image.setImageId(aImages[a]); image.setNum(a+1); image.setFlag("2");
 * imageRepository.saveAndFlush(image); }
 * 
 * //所有客诉子单提交后，要更新主单的客诉状态 //客诉状态 (1进行中 9已闭环) List<Integer> ksStatus =
 * list.stream().map(SubOrder::getCcStatus).collect(Collectors.toList());
 * ksStatus.removeAll(Collections.singleton(null)); if(ksStatus.size()>0) {
 * if(ksStatus.contains(1)) { customerComplaint.setCcStatus(1); }else {
 * if(ksStatus.contains(9)) { customerComplaint.setCcStatus(9); } } } //客诉时间
 * List<Timestamp> times =
 * list.stream().map(SubOrder::getCcTime).collect(Collectors.toList());
 * times.removeAll(Collections.singleton(null)); if(times.size()>0) {
 * customerComplaint.setCcTime(times.get(times.size()-1)); }
 * 
 * customerComplaint.setThreeSerialNo(TimeUtils.getToday());
 * 
 * //回访状态 （1需回访 2回访中 3已回访） List<Integer> hfStatus =
 * list.stream().map(SubOrder::getVisitStatus).collect(Collectors.toList());
 * hfStatus.removeAll(Collections.singleton(null)); if(hfStatus.size()>0) {
 * if(hfStatus.contains(2)) { customerComplaint.setVisitStatus(2); }else {
 * if(hfStatus.contains(1)) { customerComplaint.setVisitStatus(1); }else {
 * customerComplaint.setVisitStatus(3); } } }
 * 
 * //回访时间 List<Timestamp> visitTimes =
 * list.stream().map(SubOrder::getVisitTime).collect(Collectors.toList());
 * visitTimes.removeAll(Collections.singleton(null)); if(visitTimes.size()>0) {
 * customerComplaint.setVisitTime(visitTimes.get(visitTimes.size()-1)); }
 * 
 * //处理状态（1待处理 2处理中 3已处理） List<Integer> clStatus =
 * list.stream().map(SubOrder::getHandleStatus).collect(Collectors.toList());
 * clStatus.removeAll(Collections.singleton(null)); if(clStatus.size()>0) {
 * if(clStatus.contains(2)) { customerComplaint.setHandleStatus(2); }else {
 * if(clStatus.contains(1)) { customerComplaint.setHandleStatus(1); }else {
 * customerComplaint.setHandleStatus(3); } } }
 * 
 * //处理时间 List<Timestamp> handleTimes =
 * list.stream().map(SubOrder::getHandleTime).collect(Collectors.toList());
 * handleTimes.removeAll(Collections.singleton(null)); if(handleTimes.size()>0)
 * { customerComplaint.setHandleTime(handleTimes.get(handleTimes.size()-1)); }
 * 
 * 
 * List<String> ways =
 * list.stream().map(SubOrder::getHandleWay).collect(Collectors.toList());
 * String way = ""; for(int i=0;i<ways.size();i++) { if(i==ways.size()-1) { way
 * += ways.get(i); }else { way += ways.get(i)+","; } }
 * customerComplaint.setHandleWay(way);
 * customerComplaintRepository.saveAndFlush(customerComplaint);
 * 
 * } catch (Exception e) { logger.error("客诉保存出错:{}", e.getMessage(), e); }
 * 
 * return queryBack;
 * 
 * }
 * 
 *//**
	 * 根据条件搜索查询客诉信息
	 *
	 * @param query 搜索条件
	 *
	 * @return list
	 */
/*
 * public Page<CustomerComplaint> search(CustomerComplaintQuery query) {
 * 
 * PageRequest page = PageRequest.of(query.getPage(), query.getPageSize());
 * 
 * CustomerComplaint c = new CustomerComplaint();
 * 
 * if (query.getAccount() != null) { c.setAccount(query.getAccount()); }
 * 
 * if (query.getTransactionId() != null) {
 * c.setTransactionId(query.getTransactionId()); }
 * 
 * if (query.getTradeNo() != null) { c.setTradeNo(query.getTradeNo()); }
 * 
 * if (query.getCompany() != null) { c.setBranchCompany(query.getCompany()); }
 * if (query.getOrderNum() != null) { c.setOrderNum(query.getOrderNum()); } if
 * (query.getTotalAmount() != null) {
 * c.setEductionMoney(query.getTotalAmount()); } if (query.getVm() != null) {
 * c.setMachineNo(query.getVm()); } if (query.getUids() != null) {
 * c.setUids(query.getUids()); } if (query.getCcStatus() != null) {
 * c.setCcStatus(query.getCcStatus()); } if (query.getHandleStatus() != null) {
 * c.setHandleStatus(query.getHandleStatus()); } if (query.getVisitStatus() !=
 * null) { c.setVisitStatus(query.getVisitStatus()); } if
 * (query.getCcbeginTime() != null && query.getCcendTime()!=null) {
 * c.setCcbeginTime(query.getCcbeginTime());
 * c.setCcendTime(query.getCcendTime()); } if (query.getHandlebeginTime() !=
 * null && query.getHandleendTime()!=null) {
 * c.setHandlebeginTime(query.getHandlebeginTime());
 * c.setHandleendTime(query.getHandleendTime()); }
 * 
 * logger.info("值CustomerComplaint"+c.getUids()+c.getCcbeginTime()+c.
 * getCcendTime());
 * 
 * //Example<CustomerComplaint> condition = Example.of(where);
 * 
 * //Page<CustomerComplaint> all =
 * customerComplaintRepository.findAll(condition, page);
 * 
 * List<CustomerComplaint> collect = customerComplaintMapper.findAll(c);
 * 
 * // if (all.isEmpty() || all.getContent().isEmpty()) { // return Page.empty();
 * // }
 * 
 * // List<BasicInformation> collect = all.getContent().stream().map(o -> { //
 * BasicInformation n = new BasicInformation(); // BeanUtils.copyProperties(o,
 * n); // return n; // }).collect(Collectors.toList());
 * 
 * //返回的是主单号信息 // List<CustomerComplaint> collect =
 * all.getContent().stream().collect(Collectors.toList());
 * 
 * return new PageImpl<>(collect); }
 * 
 * public List<SubOrderAll> searchXlsx(CustomerComplaintQuery query) {
 * 
 * CustomerComplaint c = new CustomerComplaint(); if (query.getAccount() !=
 * null) { c.setAccount(query.getAccount()); } if (query.getTransactionId() !=
 * null) { c.setTransactionId(query.getTransactionId()); } if
 * (query.getTradeNo() != null) { c.setTradeNo(query.getTradeNo()); } if
 * (query.getCompany() != null) { c.setBranchCompany(query.getCompany()); } if
 * (query.getOrderNum() != null) { c.setOrderNum(query.getOrderNum()); } if
 * (query.getTotalAmount() != null) {
 * c.setEductionMoney(query.getTotalAmount()); } if (query.getVm() != null) {
 * c.setMachineNo(query.getVm()); } if (query.getUids() != null) {
 * c.setUids(query.getUids()); } if (query.getCcStatus() != null) {
 * c.setCcStatus(query.getCcStatus()); } if (query.getHandleStatus() != null) {
 * c.setHandleStatus(query.getHandleStatus()); } if (query.getVisitStatus() !=
 * null) { c.setVisitStatus(query.getVisitStatus()); } if
 * (query.getCcbeginTime() != null && query.getCcendTime()!=null) {
 * c.setCcbeginTime(query.getCcbeginTime());
 * c.setCcendTime(query.getCcendTime()); } if (query.getHandlebeginTime() !=
 * null && query.getHandleendTime()!=null) {
 * c.setHandlebeginTime(query.getHandlebeginTime());
 * c.setHandleendTime(query.getHandleendTime()); }
 * 
 * List<CustomerComplaint> list = customerComplaintMapper.findAll(c);
 * 
 * List<SubOrderAll> borderList= transList(list);
 * 
 * return borderList; }
 * 
 * public List<SubOrderAll> transList(List<CustomerComplaint> list){
 * List<SubOrderAll> allList = new ArrayList<SubOrderAll>(); for(int i
 * =0;i<list.size();i++) { String transactionId =
 * list.get(i).getTransactionId(); List<BOrder> borderList =
 * bOrderRepository.findByTransactionId(transactionId); List<SubOrderAll>
 * subOrderList = HyfUtils.convertList2List(borderList, SubOrderAll.class);
 * for(int j=0;j<subOrderList.size();j++) { SubOrderAll subOrderAll =
 * subOrderList.get(j);
 * subOrderAll.setStatus(subOrderAll.getCcStatus()!=null?subOrderAll.getCcStatus
 * ()==1?"进行中":"已闭环":"");//客诉状态
 * subOrderAll.setTime(subOrderAll.getCcTime()!=null?subOrderAll.getCcTime().
 * toString().substring(0,19):"");//客诉时间
 * subOrderAll.setProductPrice(subOrderAll.getProductPrice()!=null?subOrderAll.
 * getProductPrice().divide(BigDecimal.TEN.multiply(BigDecimal.TEN)):BigDecimal.
 * ZERO);//商品价格
 * subOrderAll.setKkTime(list.get(i).getEductionTime()!=null?list.get(i).
 * getEductionTime().toString().substring(0,19):"");//扣款时间
 * subOrderAll.setOrTime(subOrderAll.getOrderTime()!=null?subOrderAll.
 * getOrderTime().toString().substring(0,19):"");//订单时间
 * subOrderAll.setVStatus(subOrderAll.getVisitStatus()!=null?subOrderAll.
 * getVisitStatus()==1?"需回访":subOrderAll.getVisitStatus()==2?"回访中":"已回访":"");//
 * 回访状态 subOrderAll.setVTime(subOrderAll.getVisitTime()!=null?subOrderAll.
 * getVisitTime().toString().substring(0,19):"");//回访时间
 * subOrderAll.setHStatus(subOrderAll.getHandleStatus()!=null?subOrderAll.
 * getHandleStatus()==1?"待处理":subOrderAll.getHandleStatus()==2?"处理中":"已处理":"");/
 * /处理状态 subOrderAll.setHTime(subOrderAll.getHandleTime()!=null?subOrderAll.
 * getHandleTime().toString().substring(0,19):"");//处理时间
 * 
 * subOrderAll.setTransactionId(transactionId);
 * subOrderAll.setThreeSerialNo(list.get(i).getThreeSerialNo());
 * //subOrderAll.setEductionTime(list.get(i).getEductionTime());
 * subOrderAll.setMachineNo(list.get(i).getMachineNo());
 * subOrderAll.setBranchCompany(list.get(i).getBranchCompany());
 * subOrderAll.setUids(list.get(i).getUids());
 * subOrderAll.setServiceReminder(list.get(i).getServiceReminder());
 * allList.add(subOrderAll); } } return allList; }
 * 
 * 
 *//**
	 * 刷新
	 *
	 * @param query 搜索条件 过滤掉已经闭环的主单
	 * @return list
	 */
/*
 * public Page<CustomerComplaint> refresh(String user) {
 * 
 * List<CustomerComplaint> list = customerComplaintRepository.refresh(user);
 * 
 * return new PageImpl<>(list); }
 * 
 *//**
	 * 立即获取
	 *
	 * 工作台立即获取当前处理人所有的工单
	 * 
	 * @return list
	 */
/*
 * public Page<CustomerComplaint> get(String user) {
 * 
 * List<CustomerComplaint> list = customerComplaintRepository.get(user);
 * 
 * return new PageImpl<>(list); }
 * 
 *//**
	 * 立即获取 我的待办列表导出
	 *
	 * 工作台立即获取当前处理人所有的工单
	 * 
	 * @return list
	 */
/*
 * // public void getExportXlsx(String user) { // try { //
 * List<CustomerComplaint> list = customerComplaintRepository.get(user); //
 * String path = HyfUtils.exportXlsx(list); // //给当前用户发送邮件 //
 * MailUtils.sendAttachmentMail(user+"@ubox.cn", "我的待办列表", "", path); // } catch
 * (AddressException e) { // e.printStackTrace(); // } catch (MessagingException
 * e) { // e.printStackTrace(); // } catch (UnsupportedEncodingException e) { //
 * e.printStackTrace(); // } // }
 * 
 * 
 *//**
	 * 客诉详情页导出发送邮件
	 *
	 * @return list
	 */
/*
 * // public void orderDetailExportXlsx(String user,List<SubOrder> list) { //
 * try { // String path = HyfUtils.orderDetailExportXlsx(list); // //给当前用户发送邮件
 * // MailUtils.sendAttachmentMail(user+"@ubox.cn", "客诉详情页", "", path); // }
 * catch (AddressException e) { // e.printStackTrace(); // } catch
 * (MessagingException e) { // e.printStackTrace(); // } catch
 * (UnsupportedEncodingException e) { // e.printStackTrace(); // } // }
 * 
 *//**
	 * 客诉列表总表导出发送邮件
	 *
	 * @return list
	 *//*
		 * public void orderDetailAllExportXlsx(String user,List<SubOrderAll> list) {
		 * try { String path = HyfUtils.orderDetailAllExportXlsx(list); //给当前用户发送邮件
		 * MailUtils.sendAttachmentMail(user+"@ubox.cn", "客诉列表", "", path); } catch
		 * (AddressException e) { e.printStackTrace(); } catch (MessagingException e) {
		 * e.printStackTrace(); } catch (UnsupportedEncodingException e) {
		 * e.printStackTrace(); } }
		 * 
		 * // public void complaintQueryXlsx(String user,List<CustomerComplaint> list) {
		 * // try { // String path = HyfUtils.exportXlsx(list); // //给当前用户发送邮件 //
		 * MailUtils.sendAttachmentMail(user+"@ubox.cn", "客诉列表", "", path); // } catch
		 * (AddressException e) { // e.printStackTrace(); // } catch (MessagingException
		 * e) { // e.printStackTrace(); // } catch (UnsupportedEncodingException e) { //
		 * e.printStackTrace(); // } // }
		 * 
		 * 
		 * }
		 */