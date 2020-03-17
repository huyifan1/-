package com.uboxol.cloud.pandorasBox.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * model: pandorasBox
 *
 * @author liyunde
 * @since 2019/10/31 19:02
 */
@Slf4j
@Component
public class WebApiInterceptor extends HandlerInterceptorAdapter {

    private NamedThreadLocal<Long> threadLocal = new NamedThreadLocal<>("HttpRequestStartTime");

//    private static final Counter REQUEST_COUNTER = Counter.build().name(PrometheusService.SERVICE_NAME + "_http_requests_total")
////        .labelNames("path", "method", "code")
//        .labelNames("uri", "method", "status", "cost")
//        .help("requests.").register();

//    static final Gauge progressingRequests = Gauge.build()
//        .name(PrometheusService.SERVICE_NAME + "_http_inprogress_requests").labelNames("path", "method", "code")
//        .help("Inprogress requests.").register();
//
//    static final Histogram requestLatencyHistogram = Histogram.build().labelNames("path", "method", "code")
//        .name(PrometheusService.SERVICE_NAME + "_http_requests_latency_seconds_histogram").help("Request latency in seconds.")
//        .register();
//
//    static final Summary requestLatency = Summary.build()
//        .name(PrometheusService.SERVICE_NAME + "_http_requests_latency_seconds_summary")
//        .quantile(0.5, 0.05)
//        .quantile(0.9, 0.01)
//        .labelNames("path", "method", "code")
//        .help("Request latency in seconds.").register();
//
//    private Histogram.Timer histogramRequestTimer;
//
//    private Summary.Timer requestTimer;

//    private final PrometheusService service;
//
//    public WebApiInterceptor(final PrometheusService service) {
//        this.service = service;
//    }

    /**
     * This implementation always returns {@code true}.
     *
     * @param request 请求
     * @param response 响应
     * @param handler 切点对象
     */
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        threadLocal.set(System.currentTimeMillis());

        String uri = request.getRequestURI();

        logger.debug("开始请求:{}", uri);

        HandlerMethod point = (HandlerMethod) handler;

        if (point.getMethod().getDeclaredAnnotationsByType(HeaderAble.class) != null) {
            logger.debug("有 header:{}", request.getHeader("X-RequestId"));
        }

        if (point.getMethod().getDeclaredAnnotationsByType(UserParam.class) != null) {
            String user = request.getHeader("X-User");
            logger.debug("需要 user:{}", user);

            if (StringUtils.isEmpty(user)) {
                logger.error("需要X-User数据,但请求未包含");
                return false;
            }

//            MethodParameter[] parameters = point.getMethodParameters();
//
//            if (parameters.length > 0) {
//                for (MethodParameter parameter : parameters) {
//                    if (UserInjectAble.class.isAssignableFrom(parameter.getParameterType())) {
//                        ((UserInjectAble) parameter).setUser(user);
//                    }
//                }
//            }
        }


//        service.incRequestCounter();

//        progressingRequests.labels(uri, point, String.valueOf(status)).inc();
//
//        histogramRequestTimer = requestLatencyHistogram.labels(uri, point, String.valueOf(status)).startTimer();
//
//        requestTimer = requestLatency.labels(uri, point, String.valueOf(status)).startTimer();

        return super.preHandle(request, response, handler);
    }

    /**
     * This implementation is empty.
     *
     * @param request 请求
     * @param response 响应
     * @param handler 切点对象
     * @param ex 异常
     */
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {

//        String uri = request.getRequestURI();
//        String method = request.getMethod();
//        int status = response.getStatus();
//
//        logger.debug("开始请求:{}", uri);

        if (ex != null) {
            logger.error("请求出错了:", ex);
        }

//        RequestKey key = new RequestKey(uri, method, status, threadLocal.get());
//
////        service.incUriRequestCounter(key);
//
//        service.incRequestCounter();
//
//        REQUEST_COUNTER.labels(uri, method, String.valueOf(status), String.valueOf(System.currentTimeMillis() - key.getCost())).inc();

//        progressingRequests.labels(uri, method, String.valueOf(status)).dec();
//
//        histogramRequestTimer.observeDuration();
//
//        requestTimer.observeDuration();

        super.afterCompletion(request, response, handler, ex);
    }
}
