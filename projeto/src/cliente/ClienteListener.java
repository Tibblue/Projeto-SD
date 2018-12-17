package cliente;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;
import java.io.IOException;

/**
 *
 * @author KIKO
 */
public class ClienteListener implements Runnable{
    private final Socket socket;
    private final BufferedReader in;

    public ClienteListener(Socket clientSocket) throws IOException{
        this.socket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run(){
        try {
            String msg;
            // lê continuamente da entrada do socket mensagens enviadas pelo server
            while ((msg = in.readLine()) != null && !msg.equals("exit")) {
                System.out.println("[ClienteListener] Server>" + msg);
            }
            in.close();
        } catch (Exception e) {
            System.out.println("[ClienteListener] EXCEÇAO !!!");
            System.out.println(e);
        }

    }

}