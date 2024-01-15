package my.novik.telegrambotgpt.constants;

import java.util.HashMap;
import java.util.Map;

public enum Buttons {
    // Default root menu
    START("Start bot", "start", ButtonType.CALLBACK, null),
    PAY("Payment", "payment", ButtonType.CALLBACK, null),
    PSB("ПСБ", "psb", ButtonType.CALLBACK, null),
    YOO_KASSA("yooKassa", "yooKassa", ButtonType.CALLBACK, null);


    public final String bText, bCallBack, url;
    public final ButtonType bType;

    Buttons(String bText, String bCallBack, ButtonType bType, String url) {
        this.bText = bText;
        this.bCallBack = bCallBack;
        this.bType = bType;
        this.url = url;
    }

    private static final Map<String, Buttons> BUTTON_BY_CALLBACK = new HashMap<>();

    static {
        for (Buttons b: values()) {
            BUTTON_BY_CALLBACK.put(b.bCallBack, b);
        }
    }

    public static Buttons getButtonByCallback(String bCallBack) {
        return BUTTON_BY_CALLBACK.get(bCallBack);
    }
}
