package com.uboxol.cloud.pandorasBox;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.uboxol.cloud.pandorasBox.api.req.CabinetQuery;
import com.uboxol.cloud.pandorasBox.api.req.ClearGrids;
import com.uboxol.cloud.pandorasBox.api.req.Grid;
import com.uboxol.cloud.pandorasBox.api.req.OperateQuery;
import com.uboxol.cloud.pandorasBox.api.req.RiderPutMeal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApisControllerTest {

    private String HOST;

    private WebClient client;

    @Before
    public void before() {

        HOST = "http://localhost:10080";

        client = WebClient.create(HOST);
    }
    
    //1.柜子信息查询接口
    @Test
    public void testCabinetInformationQuery() {
    	CabinetQuery request = new CabinetQuery();
    	
    	List<ClearGrids> cabinetGrids = new ArrayList<ClearGrids>();
    	ClearGrids cg = new ClearGrids();
    	cg.setCabinetId("99900946");
    	List<String> grids = new ArrayList<String>();
    	grids.add("A01");
    	grids.add("A02");
    	cg.setGridIds(grids);
    	cabinetGrids.add(cg);
    	request.setCabinetGrids(cabinetGrids);
    	
        BodyInserter<CabinetQuery, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/cabinet/query";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
//    @Test
//    public void testOpenGrid() {
//    	CabinetQuery request = new CabinetQuery();
//
//    	List<String> cabinetIds = new ArrayList<String>();
//    	cabinetIds.add("99900946");
//        request.setCabinetIds(cabinetIds);
//
//        BodyInserter<CabinetQuery, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);
//
//        String uri = "/api/pandorasBox/openGrid";
//
//        String s = Post(uri, json, 10);
//
//        logger.info("返回结果:{}", s);
//    }

    //2.骑手放餐接口
    @Test
    public void riderPutMeal() {
    	RiderPutMeal request = new RiderPutMeal();
    	//场景一
//    	request.setCabinetId("99900946");
//    	request.setGridId("A03");
//    	request.setBussinessId("1");
    	
    	//场景二
    	request.setBussinessId("1");
        request.setCabinetId("99900946");
        request.setSpecific("中");

        BodyInserter<RiderPutMeal, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/rider/putMeal";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
    //3.骑手确认放餐接口
    @Test
    public void confirmPutMeal() {
    	Grid request = new Grid();
    	request.setCabinetId("99900946");
    	request.setGridId("A03");
    	request.setBussinessId("1");
    	

        BodyInserter<Grid, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/rider/confirmPutMeal";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
  //4.用户取餐接口
    @Test
    public void userTakeMeal() {
    	Grid request = new Grid();
    	request.setCabinetId("99900946");
    	request.setGridId("A03");
    	request.setBussinessId("1");
    	
        BodyInserter<Grid, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/user/takeMeal";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
  //5.二次开格接口
    @Test
    public void repeatOpenGrid() {
    	Grid request = new Grid();
    	request.setCabinetId("99900946");
    	request.setGridId("A01");
    	request.setBussinessId("1");
    	
        BodyInserter<Grid, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/repeatOpenGrid";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
    //6.客服开格接口  产品讨论中
    
    //7.清货通知接口
    @Test
    public void cleanGoods() {
    	ClearGrids request = new ClearGrids();
    	List<String> gridIds = new ArrayList<String>();
    	request.setCabinetId("99900946");
    	gridIds.add("A03");
    	//gridIds.add("A05");
    	request.setGridIds(gridIds);
    	
        BodyInserter<ClearGrids, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/cleanGoods";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
    //8.清货确认接口
    @Test
    public void confirmCleanGoods() {
    	ClearGrids request = new ClearGrids();
    	List<String> gridIds = new ArrayList<String>();
    	request.setCabinetId("99900946");
    	gridIds.add("A03");
    	gridIds.add("A01");
    	request.setGridIds(gridIds);
    	
        BodyInserter<ClearGrids, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/confirmCleanGoods";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
  //9.运营工具，查询可清货的格子
    @Test
    public void operateQuery() {
    	OperateQuery request = new OperateQuery();
    	request.setCleanStatus(1);
    	
        BodyInserter<OperateQuery, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/operateQuery";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
    @Test
    public void queryFreeGrids() {
    	ClearGrids request = new ClearGrids();
    	request.setCabinetId("99900946");
    	
        BodyInserter<ClearGrids, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/queryFreeGrids";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
    @Test
    public void cancelPutMeal() {
    	Grid request = new Grid();
    	request.setBussinessId("1");
    	request.setCabinetId("99900946");
    	request.setGridId("A03");
    	
        BodyInserter<Grid, ReactiveHttpOutputMessage> json = BodyInserters.fromObject(request);

        String uri = "/api/pandorasBox/rider/cancelPutMeal";

        String s = Post(uri, json, 10);

        logger.info("返回结果:{}", s);
    }
    
  
    
 
 
    
    
//    private String Post(String uri, BodyInserter<?, ReactiveHttpOutputMessage> json, List<NameValuePair> headers, int timeOut) {
//
//        WebClient.RequestBodySpec spec = client.post().uri(uri)
//            .contentType(MediaType.APPLICATION_JSON_UTF8)
////            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
//            ;
//
//        if (headers != null && !headers.isEmpty()) {
//            headers.forEach(nameValuePair -> spec.header(nameValuePair.getName(), nameValuePair.getValue()));
//        }
//
//        return spec.body(json)
//            .exchange()
//            .flatMap(res -> res.bodyToMono(String.class))
//            .block(Duration.ofSeconds(timeOut));
//    }
//

    
    
    private String Post(String uri, BodyInserter<?, ReactiveHttpOutputMessage> json, int timeOut) {

        return client.post().uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(json)
            .exchange()
            .flatMap(res -> res.bodyToMono(String.class))
            .block(Duration.ofSeconds(timeOut));
    }
    
    
    
    
}
