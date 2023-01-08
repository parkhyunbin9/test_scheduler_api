package com.daou.api.common.manager;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.bucket4j.Bucket;

@ExtendWith(MockitoExtension.class)
class RateLimitBucketManagerTest {

	@InjectMocks
	RateLimitBucketManager bucketManager = new RateLimitBucketManager(10, 10, 10);

	@DisplayName("설정된 토큰수보다 많은 요청을 하면 토큰을 소비하지 못한다.")
	@Test
	void rateLimitCall() {
		int testCount = 20;
		List<Boolean> result = new ArrayList<>();
		for (int i = 0; i < testCount; i++) {
			Bucket testBucket = bucketManager.getBucket();
			result.add(testBucket.tryConsume(1));
		}
		assertThat(result).contains(false);
	}

}