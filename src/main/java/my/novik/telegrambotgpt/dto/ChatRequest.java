package my.novik.telegrambotgpt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;


    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.n = 1;
        this.messages.add(new Message("user", prompt));
    }
}
