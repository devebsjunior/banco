package br.com.arq.utils;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static String getIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        return ip;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getHost(HttpServletRequest request) {
        return request.getServerName();
    }

    public static int getPort(HttpServletRequest request) {
        return request.getServerPort();
    }
}
