package my.novik.telegrambotgpt.controller;

import my.novik.telegrambotgpt.dto.ChatRequest;
import my.novik.telegrambotgpt.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ChatController {

    @Qualifier("openAIRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        ChatRequest chatRequest = new ChatRequest(model, prompt);

        ChatResponse chatResponse = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);

        if (chatResponse == null ||
                chatResponse.getChoices() == null ||
                chatResponse.getChoices().isEmpty()) {
            return "No Response";
        }

        return chatResponse.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping("/test")
    public ChatRequest test(@RequestParam String prompt) {
        ChatRequest chatRequest = new ChatRequest(model, prompt);
        return chatRequest;
    }

}
