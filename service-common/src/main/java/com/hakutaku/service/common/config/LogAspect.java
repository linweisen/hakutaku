package com.hakutaku.service.common.config;

import com.hakutaku.service.common.utils.JacksonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.linweisen.service.api.controller..*.*(..))")
    public void weblog(){}

    @Pointcut("within(com.chinaunicom.service.api.service..*) || @within(org.springframework.web.bind.annotation.RestController)")
    public void timelog() {

    }

    @Before("weblog()")
    public void updateBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        logger.info("URL:{}", request.getRequestURI());
        logger.info("token:{}", token);
        logger.info("IP:{}", request.getRemoteAddr());
        String method = request.getMethod();
        String queryString = request.getQueryString();
        Object[] args = joinPoint.getArgs();
        String params = "";
        if(args.length > 0){
            if("POST".equals(method)){
                Object object = args[0];
                Map map = getKeyAndValue(object);
                params = JacksonUtils.toJsonString(map);
            }else if("GET".equals(method) && queryString != null){
                try {
                    params = URLDecoder.decode(queryString,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("params{}:", params);
    }

    @After("weblog()")
    public void updateAfter(JoinPoint joinPoint){
//        System.out.println("delay delete cache...");
    }

    private Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val;
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    @Around("timelog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        logger.info("execute method:{}, used:{} ms",
                joinPoint.getSignature().getName(),
                stopWatch.getTotalTimeMillis());
        return  result;
    }

}
