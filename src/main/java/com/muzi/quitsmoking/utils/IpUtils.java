package com.muzi.quitsmoking.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * IP工具类
 */
@Slf4j
public class IpUtils {

    /**
     * 未知IP地址
     */
    private static final String UNKNOWN = "unknown";
    
    /**
     * 本地IP地址
     */
    private static final String LOCALHOST_IP = "127.0.0.1";
    
    /**
     * 本地IP地址完整格式
     */
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    
    /**
     * X-Forwarded-For头部
     */
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    
    /**
     * X-Real-IP头部
     */
    private static final String X_REAL_IP = "X-Real-IP";
    
    /**
     * Proxy-Client-IP头部
     */
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    
    /**
     * WL-Proxy-Client-IP头部
     */
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    
    /**
     * HTTP_CLIENT_IP头部
     */
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    
    /**
     * HTTP_X_FORWARDED_FOR头部
     */
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    
    /**
     * 获取真实IP地址
     * @param request HTTP请求
     * @return IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        
        String ip = request.getHeader(X_FORWARDED_FOR);
        
        // 获取IP地址
        if (isUnknown(ip)) {
            ip = request.getHeader(X_REAL_IP);
        }
        if (isUnknown(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (isUnknown(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (isUnknown(ip)) {
            ip = request.getHeader(HTTP_CLIENT_IP);
        }
        if (isUnknown(ip)) {
            ip = request.getHeader(HTTP_X_FORWARDED_FOR);
        }
        if (isUnknown(ip)) {
            ip = request.getRemoteAddr();
            // 如果是本地访问，则根据网卡获取本机配置的IP
            if (LOCALHOST_IP.equals(ip) || LOCALHOST_IPV6.equals(ip)) {
                ip = getLocalIp();
            }
        }
        
        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
        if (ip != null && ip.length() > 15 && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        
        return ip;
    }
    
    /**
     * 判断IP地址是否为未知
     * @param ip IP地址
     * @return 是否未知
     */
    private static boolean isUnknown(String ip) {
        return !StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip);
    }
    
    /**
     * 获取本地IP地址
     * @return 本地IP地址
     */
    private static String getLocalIp() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.error("获取本地IP地址失败", e);
            return LOCALHOST_IP;
        }
    }
    
    /**
     * 私有构造方法，防止实例化
     */
    private IpUtils() {
        throw new IllegalStateException("Utility class");
    }
} 