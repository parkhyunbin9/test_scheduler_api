package com.daou.api.common.manager;

import java.time.Duration;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RateLimitBucketManager {

	private final Bucket bucket;

	
	public RateLimitBucketManager() {
		// 1분에 10개 요청 처리  bucket
		Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));

		this.bucket = Bucket.builder()
			.addLimit(limit)
			.build();
	}

	public Bucket getBucket() {
		return bucket;
	}

	public void consumeToken() {
		if (bucket.tryConsume(1)) {
			log.info("TokenConsume Success RemainToken = {}", bucket.getAvailableTokens());
		} else {
			log.info("TOO MANY Request ");
		}

	}
}
