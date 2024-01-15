package my.novik.telegrambotgpt.constants;

import java.util.HashMap;
import java.util.Map;

public enum ChatState {

    NEW_USER(0L),
    EXISTING_USER(1L),
    ZERO_STATE(2L),
    PAID(3L),
    EPIRED(4L);

    public final Long status;

    private static final Map<Long, ChatState> BY_STATUS = new HashMap<>();

    static {
        for (ChatState bs : values()) {
            BY_STATUS.put(bs.status, bs);
        }
    }

    ChatState(Long status) {
        this.status = status;
    }

    public static ChatState valueOfStatus(Long status) {
        return BY_STATUS.get(status);
    }
}
