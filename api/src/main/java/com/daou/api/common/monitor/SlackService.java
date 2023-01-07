package com.daou.api.common.monitor;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SlackService {

	@Value(value = "${slack.token}")
	String token;
	@Value(value = "${slack.channel.monitor}")
	String channel;

	public void postSlackMessage(String message) {
		try {

			MethodsClient methods = Slack.getInstance().methods(token);
			ChatPostMessageRequest request = ChatPostMessageRequest.builder()
				.channel(channel)
				.text(message)
				.build();

			methods.chatPostMessage(request);

		} catch (SlackApiException | IOException e) {
			log.error(e.getMessage());
		}
	}

	public void postError(String message, String stackTrace) {
		postSlackMessage("배치 스케줄 진행 중 에러 발생 배치 동작 일 = " + LocalDate.now());
		postSlackMessage("에러 메세지 = " + message);
		postSlackMessage("에러 StackTrace = " + stackTrace);
	}
}
