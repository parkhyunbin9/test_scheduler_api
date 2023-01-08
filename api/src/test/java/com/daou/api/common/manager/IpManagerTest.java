package com.daou.api.common.manager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = {IpManager.class})
class IpManagerTest {

	@InjectMocks
	IpManager ipManager;
	@Mock
	private List<IpAddressMatcher> whiteList;

	@DisplayName("등록된 아이피가 아니면 deny.")
	@Test
	void getIpListFromProfileAsHashSet() {
		List<String> ipList = Arrays.asList(new String[] {"127.1.1.1", "120.100.12.1"});
		List<IpAddressMatcher> testWhiteList = ipList.stream()
			.map(IpAddressMatcher::new)
			.collect(Collectors.toList());

		ReflectionTestUtils.setField(ipManager,"whiteList", testWhiteList);

		assertThat(ipManager.allow("127.1.1.1")).isTrue();
		assertThat(ipManager.allow("127.1.1.0")).isFalse();


	}


}