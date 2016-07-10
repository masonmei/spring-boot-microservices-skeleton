package com.igitras.common.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mason on 7/10/16.
 */
public abstract class RequestUtil {

    /**
     * Get the remote address from {@HttpServletRequest}.
     *
     * @param request {@HttpServletRequest}
     *
     * @return remote address.
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
