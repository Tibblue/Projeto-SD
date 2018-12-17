package cliente;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

/**
 *
 * @author KIKO
 */
public class ClienteWriter implements Runnable{
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader systemIn;

    public ClienteWriter(Socket clientSocket) throws IOException{
        this.socket = clientSocket;
        this.out = new PrintWriter(socket.getOutputStream());
        this.systemIn = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(){
        try {
            String msg;
            // lê continuamente do STDIN, e envia para o servidor
            while ((msg = systemIn.readLine()) != null && !msg.equals("exit")) {
                System.out.println("[ClienteWriter] Enviado> "+msg);
                out.println(msg);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("[ClienteWriter] EXCEÇAO !!!");
            System.out.println(e);
        }

        out.close();
    }

}