package my.novik.telegrambotgpt.constants;

public enum Commands {

    START("/start"),
    PAY("/pay"),
    PSB("psb"),
    YOO_KASSA("yooKassa");

    public final String commandName;


    Commands(String commandName) {
        this.commandName = commandName;
    }

    public String getCommand() {
        return commandName;
    }
}
