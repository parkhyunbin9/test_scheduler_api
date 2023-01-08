package com.daou.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import com.daou.api.common.manager.IpManager;
import com.daou.api.common.monitor.SlackService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IpService {

	private final SlackService slackService;
	private final IpManager ipManager;


	public boolean isAccessible(String ip) {
		List<IpAddressMatcher> accessibleIpList = ipManager.getIpAddressMatchers();
		for (int i = 0; i < accessibleIpList.size(); i++) {
			log.info(ip);
			log.info(String.valueOf(accessibleIpList.get(i).matches(ip)));
		}
		return accessibleIpList.stream().anyMatch(accessibleIp -> accessibleIp.matches(ip));
	}

}
