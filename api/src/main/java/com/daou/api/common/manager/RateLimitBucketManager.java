package com.daou.api.common.manager;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RateLimitBucketManager {

	private int duration;
	private int tokenCount;
	private int capacity;
	private Bucket bucket;

	public RateLimitBucketManager(
		@Value(value = "${rate-limit.duration-sec}") int duration,
		@Value(value = "${rate-limit.token-count}") int tokenCount,
		@Value(value = "${rate-limit.capacity}") int capacity) {

		Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(tokenCount, Duration.ofSeconds(duration)));
		this.bucket = Bucket.builder()
			.addLimit(limit)
			.build();
	}

	public Bucket getBucket() {
		return bucket;
	}

}
