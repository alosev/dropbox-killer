package models.messages;

import java.io.*;
import java.util.Base64;

public class MessageFile extends Message {
    private String name;
    private String context;

    public MessageFile(File file) throws IOException {
        super(MessageType.FILE);
        this.name = file.getName();

        byte[] buffer = new byte[2048];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        while (bis.read(buffer) != -1){
            bos.write(buffer);
        }

        context = Base64.getEncoder().encodeToString(bos.toByteArray());

        bis.close();
        bos.close();
    }

    public String getName() {
        return name;
    }

    public byte[] getContext() {
        return Base64.getDecoder().decode(context);
    }
}
