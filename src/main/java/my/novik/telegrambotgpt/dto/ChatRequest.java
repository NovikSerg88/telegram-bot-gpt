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

    private String model; // ID of the model to use
    private List<Message> messages; // a list of messages comprising to the conversation so far
    private int n; //how many chat completion choices to generate for each input message.
    private double temperature; //what sampling temperature to use, between 0 and 2.


    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.n = 1;
        this.messages.add(new Message("user", prompt));
    }
}
