package com.ruoyi.framework.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

@Component
public class RequestMINIInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(RequestMINIInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;

                String servletPath = request.getServletPath();

                if (servletPath.toLowerCase().contains("notify") || servletPath.toLowerCase().contains("uploadfile") || servletPath.toLowerCase().contains("uploadmusic") || servletPath.toLowerCase().contains("getmatchteaminviteercode")) {
                    return true;
                }
                Map<String, String> headerMap = new HashMap<>();
                Enumeration<String> headers = request.getHeaderNames();
                while (headers.hasMoreElements()) {
                    String headName = headers.nextElement();
                    String headValue = request.getHeader(headName);
                    headerMap.put(headName, headValue);
                }
                String sign = headerMap.get("sign");
                String ct = headerMap.get("ct");

                Map<String, String[]> params = request.getParameterMap();
                String paramStr = "";
                if (params != null) {

                    SortedMap<String, String> param = new TreeMap<>();
                    for (Map.Entry<String, String[]> entry : params.entrySet()) {
                        param.put(entry.getKey(), entry.getValue()[0]);
                    }
                    param.put("ct", ct);
                    param.put("salt", "4gaaaajga#hhjj666777744@@ggjjj");

                    Iterator iterator = param.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if(StringUtils.isNotEmpty(key) && !"{}".equals(key)){
                            String value = param.get(key);
                            paramStr += (key + value);
                        }
                    }
                }

                paramStr += servletPath;
                paramStr = paramStr.replaceAll(" ", "");
                paramStr = URLEncoder.encode(paramStr, "utf-8");
                String sign1 = MD5Encode(paramStr, "UTF-8").toLowerCase();
                if(sign1.equals(sign)){
                    return true;
                }else{
                    _response(response);
                }

            }
        } catch (Exception e) {
            log.error("(小程序) 请求异常 ex:{}", e.getMessage());
            _response(response);
        }
        return true;
    }

    public void _response(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        // R.fail 是接口响应统一封装返回
        response.getWriter().println(objectMapper.writeValueAsString(R.fail("哈哈哈")));
        return;
    }


    private String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


}
