package my.novik.telegrambotgpt.constants;

import java.util.HashMap;
import java.util.Map;

public enum UpdateType {
    COMMAND(0L),
    MESSAGE(1L),
    CALLBACK_QUERY(2L),
    UNKNOWN(3L);

    public final Long updateType;

    private static Map<Long, UpdateType> updateTypes = new HashMap<>();

    UpdateType(Long updateType) {
        this.updateType = updateType;
    }

    static {
        for (UpdateType ut : values()) {
            updateTypes.put(ut.updateType, ut);
        }
    }

    public static UpdateType valueOfUpdateType(Long updateType) {
        return updateTypes.get(updateType);
    }
}
