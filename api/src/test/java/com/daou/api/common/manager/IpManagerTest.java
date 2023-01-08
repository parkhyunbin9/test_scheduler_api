package com.daou.api.common.manager;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class IpManagerTest {

	@InjectMocks
	IpManager ipManager;
	@Mock
	private List<String> whiteList;

	@DisplayName("등록된 아이피가 아니면 deny.")
	@Test
	void denyNotAllowIp() {
		List<String> testIpList = Arrays.asList(new String[] {"127.1.1.1", "120.100.12.1"});

		ReflectionTestUtils.setField(ipManager, "whiteList", testIpList);

		assertThat(ipManager.allow("127.1.1.1")).isTrue();
		assertThat(ipManager.allow("127.1.1.0")).isFalse();

	}

	@DisplayName("범위내의 아이피에 속하지 않으면 아니면 deny.")
	@Test
	void denyIpNotInRange() {
		List<String> testIpList = Arrays.asList(new String[] {"127.1.1.1/3", "120.100.12.1"});
		ReflectionTestUtils.setField(ipManager, "whiteList", testIpList);
		assertThat(ipManager.allow("127.1.1.1")).isTrue();
		assertThat(ipManager.allow("127.1.1.2")).isTrue();
		assertThat(ipManager.allow("127.1.1.3")).isTrue();
		assertThat(ipManager.allow("127.1.1.0")).isFalse();

	}

	@DisplayName("모든 대역대의 아이피 허용.")
	@Test
	void allowStarPattern() {
		List<String> testIpList = Arrays.asList(new String[] {"127.1.1.*", "120.100.12.1"});
		ReflectionTestUtils.setField(ipManager, "whiteList", testIpList);
		assertThat(ipManager.allow("127.1.1.1")).isTrue();
		assertThat(ipManager.allow("127.1.1.2")).isTrue();
		assertThat(ipManager.allow("127.1.1.3")).isTrue();
		assertThat(ipManager.allow("127.1.1.0")).isTrue();
		assertThat(ipManager.allow("127.1.2.0")).isFalse();

	}
}