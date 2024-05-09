package com.example.tripbridgeserver.controller;

import com.example.tripbridgeserver.dto.ChatGPTRequest;
import com.example.tripbridgeserver.dto.ChatGPTResponse;
import com.example.tripbridgeserver.entity.ChatRoute;
import com.example.tripbridgeserver.repository.ChatRouteRepository;
import com.example.tripbridgeserver.repository.RouteRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import com.example.tripbridgeserver.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
public class ChatBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RouteService routeService;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final ChatRouteRepository chatRouteRepository;
    private RestTemplate restTemplate;

    @Autowired
    public ChatBotController(RouteService routeService, RouteRepository routeRepository, UserRepository userRepository, ChatRouteRepository chatRouteRepository, RestTemplate restTemplate){
        this.routeService = routeService;
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.chatRouteRepository = chatRouteRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/chatBot/question3")
    public String generatePromptForAll() {
        List<ChatRoute> chatRoutes = chatRouteRepository.findAll();

        if (chatRoutes.isEmpty()) {
            return "저장된 장소 정보가 없습니다.";
        }

        StringBuilder promptBuilder = new StringBuilder();
        for (ChatRoute chatRoute : chatRoutes) {
            // 프롬프트 생성
            promptBuilder.append(chatRoute.getPlace() + ",");
        }
        promptBuilder.append("을(를) 순서대로 방문할 때의 이동 방법과 예상 비용을 500자 이내로 알려줘.\n");

        ChatGPTRequest request = new ChatGPTRequest(model, promptBuilder.toString());
        ChatGPTResponse chatGPTResponse =  restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping("/chatBot/question4")
    public String generateSchedule(@RequestBody String schedule) {
        List<ChatRoute> chatRoutes = chatRouteRepository.findAll();

        if (chatRoutes.isEmpty()) {
            return "저장된 장소 정보가 없습니다.";
        }

        StringBuilder promptBuilder = new StringBuilder();
        for (ChatRoute chatRoute : chatRoutes) {
            // 프롬프트 생성
            promptBuilder.append(chatRoute.getPlace() + ",");
        }
        promptBuilder.append("을(를) 순서대로 방문할 꺼야");
        promptBuilder.append(schedule);
        promptBuilder.append(" 으로 일정을 표로 생성해줘\\n");


        ChatGPTRequest request = new ChatGPTRequest(model, promptBuilder.toString());
        ChatGPTResponse chatGPTResponse =  restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }


}
