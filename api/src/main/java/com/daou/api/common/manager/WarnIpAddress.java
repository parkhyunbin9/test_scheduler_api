package com.daou.api.common.manager;

import java.util.LinkedList;
import java.util.Queue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class WarnIpAddress {

	// 5초 내 3번 이상 잘못 요청시
	public static int LIMIT_WARN_COUNT = 3;
	public static int LIMIT_WARN_DURATION = 5000;
	private final Queue<Integer> requestCount = new LinkedList<>();
	private String ip;
	private long requestTimestamp;

	public WarnIpAddress(String ip, Long requestTimestamp) {
		this.ip = ip;
		setRequestTimestamp(requestTimestamp);
		addRequestCount();
	}

	public synchronized void addRequestCount() {
		if (requestCount.size() == LIMIT_WARN_COUNT) {
			requestCount.clear();
			requestCount.add(1);
		} else {
			requestCount.add(1);
		}
	}

	public synchronized void setRequestTimestamp(Long currentRequestTime) {
		requestTimestamp = currentRequestTime;
	}

	public void resetRequestTimestamp(Long currentRequestTime) {
		if (!isSmallerThanLimitTime(currentRequestTime)) {
			setRequestTimestamp(currentRequestTime);
		}
	}

	public boolean shouldNotice(Long currentRequestTime) {
		return isSmallerThanLimitTime(currentRequestTime)
			&& requestCount.size() == LIMIT_WARN_COUNT;
	}

	public void init(long requestTimestamp) {
		setRequestTimestamp(requestTimestamp);
		addRequestCount();
	}

	private boolean isSmallerThanLimitTime(Long currentRequestTime) {
		return LIMIT_WARN_DURATION >= currentRequestTime - requestTimestamp;
	}
}
