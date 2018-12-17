package cliente;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author KIKO
 */
public class ClienteConnection{
    private static int port;
    
    public ClienteConnection(int port){
        this.port = port;
    }
    
    public void connect(){
        try (Socket socket = new Socket("127.0.0.1", 1234)) {
            Thread writer = new Thread(new ClienteWriter(socket));
            Thread listener = new Thread(new ClienteListener(socket));
            writer.start();
            listener.start();
            try {
                writer.join();
                listener.join();
            } catch (InterruptedException e) {
                System.out.println("[Cliente] Execução interrompida!!!");
                System.out.println(e);
            }
            socket.close();
            System.out.println("[Cliente] Ligação terminada!!!");
        }
        catch (IOException e) {
            System.out.println("[Cliente] EXCEÇAO !!!");
            System.out.println(e);
        }

    }

}