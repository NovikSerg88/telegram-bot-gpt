package my.novik.telegrambotgpt.constants;

public enum Messages {

    START_MESSAGE("Чат бот для взаимодействия с API OPEN AI"),

    PAY_MESSAGE("Выберите способ оплаты:");

    public final String text;


    Messages(String text) {
        this.text = text;
    }
}
