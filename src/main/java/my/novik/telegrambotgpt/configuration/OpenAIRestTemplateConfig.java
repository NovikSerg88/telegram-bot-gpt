package my.novik.telegrambotgpt.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIRestTemplateConfig {

    @Value("${openai.api.key}")
    private String opeAIApiKey;

    @Bean
    @Qualifier("openAIRestTemplate")
    public RestTemplate openaiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + opeAIApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

}
