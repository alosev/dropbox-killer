package models.messages;

public class MessageLogin extends Message {
    private String login;
    private String password;

    public MessageLogin(String login, String password) {
        super(MessageType.LOGIN);

        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
