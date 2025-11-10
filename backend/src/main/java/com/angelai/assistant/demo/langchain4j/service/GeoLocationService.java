package com.angelai.assistant.demo.langchain4j.service;

import com.angelai.assistant.demo.langchain4j.dto.IpAddressInfoDto;
import com.angelai.assistant.demo.langchain4j.util.HttpUtil;
import com.angelai.assistant.demo.langchain4j.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationService {

    @Value("${llm.tool.ip-location.url}")
    private String ipLocationUrl;

    /**
     * 根据IP地址获取地理位置信息（使用太平洋网络IP查询接口）
     * @param ip IP地址
     * @return 地理位置信息
     */
    public String getLocationByIp(String ip) {
        // 对于本地测试环境，返回本地地址
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "本地地址";
        }
        
        if (ip != null && !ip.isEmpty()) {
            // 这里可以添加更多IP段与地理位置的映射逻辑
            if (ip.startsWith("192.168.")) {
                return "局域网地址";
            } else if (ip.startsWith("10.")) {
                return "内网地址";
            } else {
                // 调用太平洋网络IP查询接口
                try {
                    String url = ipLocationUrl + "?json=true&ip=" + ip;
                    String response = HttpUtil.get(url);
                    
                    if (response != null) {
                        IpAddressInfoDto dto = JsonUtil.fromJson(response, IpAddressInfoDto.class);
                        if (dto.getAddr() != null && !dto.getAddr().isEmpty()) {
                            return dto.getAddr();
                        }
                    }
                } catch (Exception e) {
                    // 如果调用接口失败，返回默认信息
                    return "未知地区 (IP: " + ip + ")";
                }
            }
        }
        
        return "未知位置";
    }

    /**
     * 从请求中获取客户端真实IP地址
     * @param remoteAddr 远程地址
     * @param xForwardedFor X-Forwarded-For头
     * @param xRealIp X-Real-IP头
     * @return 客户端真实IP地址
     */
    public String getRealIpAddress(String remoteAddr, String xForwardedFor, String xRealIp) {
        String ipAddress = remoteAddr;
        
        // 处理X-Forwarded-For头部
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            // X-Forwarded-For可能包含多个IP地址，取第一个
            int index = xForwardedFor.indexOf(",");
            if (index != -1) {
                ipAddress = xForwardedFor.substring(0, index);
            } else {
                ipAddress = xForwardedFor;
            }
        }
        
        // 处理X-Real-IP头部
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp) && (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress))) {
            ipAddress = xRealIp;
        }
        
        // 处理IPv6本地地址
        if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        
        return ipAddress;
    }
}