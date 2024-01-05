package my.novik.telegrambotgpt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usage {

    private int completionTokens;
    private int promptTokens;
    private int totalTokens;

}
