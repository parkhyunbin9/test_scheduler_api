package com.daou.api.common.interceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.daou.api.common.manager.RateLimitBucketManager;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

	private final RateLimitBucketManager bucketManager;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		Bucket bucket = bucketManager.getBucket();
		ConsumptionProbe consumeResult = bucket.tryConsumeAndReturnRemaining(1);
		if (consumeResult.isConsumed()) {

			response.addHeader("X-Rate-Limit-Remaining",
				String.valueOf(consumeResult.getRemainingTokens()));
			return true;
		} else {

			response.addHeader("Retry-after", String.valueOf(
				TimeUnit.SECONDS.convert(consumeResult.getNanosToWaitForRefill(),
					TimeUnit.NANOSECONDS)));

			response.addHeader("X-Rate-Limit-Reset", String.valueOf(
				TimeUnit.SECONDS.convert(consumeResult.getNanosToWaitForReset(),
					TimeUnit.NANOSECONDS)));

			response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
				"현재 요청을 처리할 수 없습니다. 잠시후에 시도해 주세요.");
			return false;
		}
	}
}
