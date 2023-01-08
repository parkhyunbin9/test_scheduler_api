package com.daou.api.common.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
public class IpManager {
	@Value(value = "${auth.allowIp}")
	private List<String > whiteList;

	public boolean allow(String requestIp) {
		return whiteList.stream().anyMatch(allowIp -> {
				try {
					if(allowIp.contains("*") || allowIp.contains("/")){
						return checkIpBand(allowIp, requestIp);
					} else return allowIp.matches(requestIp);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
	}

	public boolean allow(HttpServletRequest request) {
		String clientIp = getClientIp(request);
		return allow(clientIp);
	}

	public static String getClientIp(HttpServletRequest request) {
		String ip = StringUtils.trimToNull(request.getHeader("X-Forwarded-For"));

		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("x-real-ip");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("x-original-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public boolean checkIpBand( String ip, String userIp ) throws Exception {

		boolean result = true;
		String[] ipArr = ip.split("\\.");
		String[] userIpArr = userIp.split("\\.");

		for (int i = 0; i < ipArr.length; i++) {
			if (ipArr[i].indexOf("/") >= 0) {
				String[] tempArr = ipArr[i].split("/");
				boolean tResult = false;
				Integer startNum = Integer.valueOf(tempArr[0]);
				Integer endNum = Integer.valueOf(tempArr[1]);

				for (int j = startNum; j <= endNum; j++) {
					if (String.valueOf(j).equals(userIpArr[i])) {
						tResult = true;
					}
				}
				if (!tResult) {result = false;}
			} else {
				if (!ipArr[i].equals(userIpArr[i]) && !ipArr[i].equals("*")) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
}
