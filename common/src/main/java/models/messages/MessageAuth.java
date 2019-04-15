package models.messages;

import models.Customer;

public class MessageAuth extends Message {
    private Customer credentials;

    public MessageAuth(Customer credentials) {
        super(MessageType.AUTH);

        this.credentials = credentials;
    }

    public Customer getCredentials() {
        return credentials;
    }
}
