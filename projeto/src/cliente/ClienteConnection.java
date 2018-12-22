package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author KIKO
 */
public class ClienteConnection{
    private final int PORT;
    private Socket socket;
    private Thread writer;
    private Thread listener;
    
    public ClienteConnection(int port){
        this.PORT = port;
    }
    
    public boolean connect(String email, String password){
        try {
            socket = new Socket("127.0.0.1", 1234);
            // autenticao
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("LOGIN " + email + " " + password);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String login = in.readLine();
                System.out.println(login);
            if( login.equals("SUCCESS") ){
                // TODO receber o USER INFO mandado pelo servidor
                
                System.out.println("[ClienteCon] Login OK");
                this.writer = new Thread(new ClienteWriter(socket));
                this.listener = new Thread(new ClienteListener(socket));
                this.writer.start();
                this.listener.start();
                return true;
//                try {
//                    this.writer.join();
//                    this.listener.join();
//                } catch (InterruptedException e) {
//                    System.out.println("[ClienteCon] Execução interrompida!!!");
//                    System.out.println(e);
//                }
            }
            else{
                System.out.println("[ClienteCon] Email/Password incorretos!!!");
                this.socket.close();
                return false;
            }
        }
        catch (IOException e) {
            System.out.println("[ClienteCon] Socket RIP !!!");
            System.out.println(e);
            return false;
        }
    }
    
    public void closeConnection(){
        try {
            this.writer.stop();
            this.listener.stop();
            this.socket.close();
            System.out.println("[ClienteCon] Ligação terminada com sucesso!!!");
        } catch (IOException e) {
            System.out.println("[ClienteCon] CloseConnection RIP!!!");
            System.out.println(e);
        }
    }
    
}