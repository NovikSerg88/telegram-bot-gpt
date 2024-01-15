package my.novik.telegrambotgpt.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.SendInvoice;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.novik.telegrambotgpt.constants.ButtonType;
import my.novik.telegrambotgpt.constants.Buttons;
import my.novik.telegrambotgpt.constants.ChatState;
import my.novik.telegrambotgpt.constants.Commands;
import my.novik.telegrambotgpt.constants.Messages;
import my.novik.telegrambotgpt.constants.UpdateType;
import my.novik.telegrambotgpt.dto.ChatRequest;
import my.novik.telegrambotgpt.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBotUpdateListener implements UpdatesListener {

    private final TelegramBot telegramBot;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${psb.token}")
    private String psb;

    @Value("${yookassa.token}")
    private String yooKassa;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            Long chatId = 0L;
            String firstName = null;
            String lastName = null;
            String userName = null;
            ChatState chatState = null;

            UpdateType updateType = updateProcessing(update);

            if (updateType == UpdateType.COMMAND || updateType == UpdateType.MESSAGE) {
                firstName = update.message().from().firstName();
                lastName = update.message().from().lastName();
                userName = update.message().from().username();
                chatId = update.message().from().id();
            } else if (updateType == UpdateType.CALLBACK_QUERY) {
                firstName = update.callbackQuery().from().firstName();
                lastName = update.callbackQuery().from().lastName();
                userName = update.callbackQuery().from().username();
                chatId = update.callbackQuery().from().id();
            }
            commandProcessing(chatId, update, updateType);

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private UpdateType updateProcessing(Update update) {
        if (update.message() != null) { // Message processing
            if (update.message().text() != null) {
                if (update.message().text().startsWith("/")) {
                    return UpdateType.COMMAND;
                } else {
                    return UpdateType.MESSAGE;
                }
            } else {
                return UpdateType.UNKNOWN;
            }
        } else if (update.callbackQuery() != null) { //Callback answer processing
            return UpdateType.CALLBACK_QUERY;
        } else {
            return UpdateType.UNKNOWN;
        }
    }

    private void commandProcessing(Long chatId, Update update, UpdateType updateType) {
        if (updateType == UpdateType.MESSAGE) {
            String prompt = update.message().text();
            ChatRequest chatRequest = new ChatRequest(model, prompt);
            ChatResponse chatResponse = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            if (chatResponse == null ||
                    chatResponse.getChoices() == null ||
                    chatResponse.getChoices().isEmpty()) {
                sendMessage(chatId, "No Response");
            }
            sendMessage(chatId, chatResponse.getChoices().get(0).getMessage().getContent());
        } else if (updateType == UpdateType.COMMAND && update.message().text().equalsIgnoreCase(Commands.START.getCommand())) {
            sendMessage(chatId, Messages.START_MESSAGE.text);
        } else if (updateType == UpdateType.COMMAND && update.message().text().equalsIgnoreCase(Commands.PAY.getCommand())) {
            sendMenu(chatId, update.message().messageId(), "Выберите способ оплаты:", Buttons.YOO_KASSA, Buttons.PSB);
        } else if (updateType == UpdateType.CALLBACK_QUERY && update.callbackQuery().data().equalsIgnoreCase("psb")) {
            sendInvoice(chatId, psb);
        } else if (updateType == UpdateType.CALLBACK_QUERY && update.callbackQuery().data().equalsIgnoreCase("yooKassa")) {
            sendInvoice(chatId, yooKassa);
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse response = telegramBot.execute(sendMessage);
        if (response != null && !response.isOk()) {
            log.error("Message was not sent. Error code: " + response.errorCode());
        } else if (response == null) {
            log.error("Response is null");
        }
    }

    private void sendMenu(Long chatId, Integer messageId, String menuHeader, Buttons... butons) {
        InlineKeyboardButton[][] inlineKeyboardButtons = new InlineKeyboardButton[butons.length][1];
        for (int i = 0; i < butons.length; i++) {
            if (butons[i].bType.equals(ButtonType.CALLBACK)) {
                inlineKeyboardButtons[i][0] = new InlineKeyboardButton(butons[i].bText).callbackData(butons[i].bCallBack);
            } else {
                inlineKeyboardButtons[i][0] = new InlineKeyboardButton(butons[i].bText).url(butons[i].url);
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardButtons);
            SendMessage message = new SendMessage(chatId, menuHeader);
            message.replyMarkup(inlineKeyboardMarkup);
            message.replyToMessageId(messageId);
            SendResponse response = telegramBot.execute(message);
            if (response != null && !response.isOk()) {
                log.error("Message was not sent. Error code: " + response.errorCode());
            } else if (response == null) {
                log.error("Response is null");
            }
        }
    }

    private void sendInvoice(Long chatId, String token) {
        SendInvoice sendInvoice = new SendInvoice(chatId, "Оплата токенов", "Для оплаты перейдите по ссылке", "token_payment",
                token, "my_start_param", "RUB", new LabeledPrice("RUB", 20000))
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Оплатить").pay()));
        SendResponse response = telegramBot.execute(sendInvoice);
        if (response != null && !response.isOk()) {
            log.error("Message was not sent. Error code: " + response.errorCode());
        } else if (response == null) {
            log.error("Response is null");
        }
    }
}
