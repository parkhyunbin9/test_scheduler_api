package com.daou.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import com.daou.api.common.manager.IpManager;
import com.daou.api.common.manager.WarnIpAddress;
import com.daou.api.common.monitor.SlackService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IpService {

	private final SlackService slackService;
	private final IpManager ipManager;

	Map<String, WarnIpAddress> warnIpMap = new HashMap<>();

	public boolean isAccessible(String ip) {
		List<IpAddressMatcher> accessibleIpList = ipManager.getIpAddressMatchers();
		for (int i = 0; i < accessibleIpList.size(); i++) {
			log.info(ip);
			log.info(String.valueOf(accessibleIpList.get(i).matches(ip)));
		}
		return accessibleIpList.stream().anyMatch(accessibleIp -> accessibleIp.matches(ip));
	}

	public void unAccessibleIpRequest(String ip) {
		WarnIpAddress remainWarnIp = warnIpMap.get(ip);
		long warnTimestamp = System.currentTimeMillis();
		if (Objects.nonNull(remainWarnIp)) {
			if (remainWarnIp.shouldNotice(warnTimestamp)) {
				// 임계 시간내 임계횟수 이상 호출 -> 알림 필요!!
				slackService.postSlackMessage(
					"잘못된 IP : " + ip + " 가 " + (WarnIpAddress.LIMIT_WARN_DURATION / 1000) + "초 이내 "
						+ WarnIpAddress.LIMIT_WARN_COUNT + " 번 이상 잘못된 호출을 반복하였습니다");
				remainWarnIp.init(warnTimestamp);
			} else {
				remainWarnIp.addRequestCount();
				remainWarnIp.resetRequestTimestamp(warnTimestamp);
			}
			log.info("remainWarnIp = {}", remainWarnIp);
			warnIpMap.put(ip, remainWarnIp);
		} else {
			WarnIpAddress newWarnIp = new WarnIpAddress(ip, warnTimestamp);
			newWarnIp.addRequestCount();
			warnIpMap.put(ip, newWarnIp);
		}
		log.info("warnIpMap = {}", warnIpMap);
	}

}
