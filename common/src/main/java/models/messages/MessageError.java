package models.messages;

public class MessageError extends Message {
    private int code;
    private String text;

    public MessageError(int code, String text) {
        super(MessageType.ERROR);

        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
