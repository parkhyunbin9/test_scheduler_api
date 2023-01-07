package com.daou.api.common.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IpManager {

	@Value(value = "${auth.allowIp}")
	private final List<String> allowIp;

	private List<IpAddressMatcher> ipAddressMatchers = new ArrayList<>();

	public List<IpAddressMatcher> getIpAddressMatchers() {
		if (ipAddressMatchers.isEmpty()) {
			this.ipAddressMatchers = allowIp.stream().map(IpAddressMatcher::new)
				.collect(Collectors.toList());
		}
		return ipAddressMatchers;
	}

}
