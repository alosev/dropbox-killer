package models.messages;

import java.io.*;

public class Message implements Serializable {
    private MessageType type;

    public Message(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public static void serialization(Message msg, OutputStream out) throws IOException {
        ObjectOutputStream msgOutput = new ObjectOutputStream(out);
        msgOutput.writeObject(msg);
    }

    public static Message deserialization(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream msgInput = new ObjectInputStream(in);
        return (Message) msgInput.readObject();
    }

}
