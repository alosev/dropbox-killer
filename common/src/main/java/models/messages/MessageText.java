package models.messages;

public class MessageText extends Message {
    private String context;

    public MessageText(String context) {
        super(MessageType.TEXT);

        this.context = context;
    }

    public String getContext() {
        return context;
    }
}
