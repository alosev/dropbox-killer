import entity.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainServer {
    public static void main(String[] args) throws IOException {
        init();
    }

    private static void init() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s;

        Server server = new Server(8196, System.out::println);
        server.run();

        while (true){
            s = reader.readLine();

            if(s.startsWith("/end")){
                server.stop();
                break;
            }
        }

    }
}
