package com.example.tripbridgeserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.tripbridgeserver.dto.ChatGPTRequest;
import com.example.tripbridgeserver.dto.ChatGPTResponse;
import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.ChatRouteRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatBotService {

	@Value("${openai.model}") // gpt 모델
	private String model;

	@Value("${openai.api.url}")
	private String apiURL;

	private final UserRepository userRepository;
	private final ChatRouteRepository chatRouteRepository;
	private RestTemplate restTemplate;

	public String generateNearPlaces(String choicePlace) {
		StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append(choicePlace);
		promptBuilder.append("의 주소를 기반으로 5km 이내의 주변 관광지 5곳을 추천. 가독성을 위해 한 줄 씩 나오도록 고정.\n");

		ChatGPTRequest request = new ChatGPTRequest(model, promptBuilder.toString());
		ChatGPTResponse chatGPTResponse =  restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
		return chatGPTResponse.getChoices().get(0).getMessage().getContent();
	}

	public String generatePlaceDetail(String choicePlace) {

		StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append(choicePlace);
		promptBuilder.append("의 이용방법과 비용 등 상세정보를 알려줘. 가독성을 위해 문단 나눠서 보여줄 것.\n");

		ChatGPTRequest request = new ChatGPTRequest(model, promptBuilder.toString());
		ChatGPTResponse chatGPTResponse =  restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
		return chatGPTResponse.getChoices().get(0).getMessage().getContent();
	}

	public String generateTransferAndCost() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		User currentUser = userRepository.findByEmail(userEmail);

		List<ChatRoute> chatRoutes = chatRouteRepository.findByUserOrderByRouteOrder(currentUser);

		if (chatRoutes.isEmpty()) {
			return "저장된 장소 정보가 없습니다.";
		}

		StringBuilder promptBuilder = new StringBuilder();
		for (ChatRoute chatRoute : chatRoutes) {
			promptBuilder.append(chatRoute.getPlace() + ",");
		}
		promptBuilder.append("을(를) 순서대로 방문할 때 이동 수단과 예상 비용을 500자 이내로 알려줘.\n");
		log.info(promptBuilder.toString());

		ChatGPTRequest request = new ChatGPTRequest(model, promptBuilder.toString());
		ChatGPTResponse chatGPTResponse =  restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
		return chatGPTResponse.getChoices().get(0).getMessage().getContent();
	}

	public String generateSchedule(String schedule) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		User currentUser = userRepository.findByEmail(userEmail);

		List<ChatRoute> chatRoutes = chatRouteRepository.findByUserOrderByRouteOrder(currentUser);

		if (chatRoutes.isEmpty()) {
			return "저장된 장소 정보가 없습니다.";
		}

		StringBuilder promptBuilder = new StringBuilder();
		for (ChatRoute chatRoute : chatRoutes) {
			promptBuilder.append(chatRoute.getPlace() + ",");
		}
		promptBuilder.append("을(를) 순서대로 방문할 예정. ");
		promptBuilder.append(schedule);
		promptBuilder.append(" 일정을 날짜별로 추천.\\n");
		log.info(promptBuilder.toString());

		ChatGPTRequest request = new ChatGPTRequest(model, promptBuilder.toString());
		ChatGPTResponse chatGPTResponse =  restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
		return chatGPTResponse.getChoices().get(0).getMessage().getContent();
	}
}
