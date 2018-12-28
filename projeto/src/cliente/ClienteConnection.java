package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import servidor.Server;

/**
 *
 * @author KIKO
 */
public class ClienteConnection{
    private final int PORT;
    private Socket socket;
    private Thread writer;
    private Thread listener;
    private PrintWriter out;
    private BufferedReader in;
    private User user;
    
    public ClienteConnection(int port){
        this.PORT = port;
        this.user = null;
    }
    
    public String connect(String email, String password){
        try {
            this.socket = new Socket("127.0.0.1", 1234);
            // autenticao
            this.out = new PrintWriter(socket.getOutputStream());
            this.out.println("LOGIN " + email + " " + password);
            this.out.flush();
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String login = this.in.readLine();
            System.out.println(login);
            if( login.equals("SUCCESS") ){
                System.out.println("[ClienteCon] Login OK");
//                this.writer = new Thread(new ClienteWriter(socket));
//                this.listener = new Thread(new ClienteListener(socket));
//                this.writer.start();
//                this.listener.start();
                return "SUCCESS";
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
                return "FAIL";
            }
        }
        catch (IOException e) {
            System.out.println("[ClienteCon] Socket RIP !!!");
            System.out.println(e);
            return "FAIL";
        }
    }
    
    public void closeConnection(){
        try {
//            this.writer.stop();
//            this.listener.stop();
            this.socket.close();
            System.out.println("[ClienteCon] Ligação terminada com sucesso!!!");
        } catch (IOException e) {
            System.out.println("[ClienteCon] CloseConnection RIP!!!");
            System.out.println(e);
        }
    }
    
    // recebe String com pedido para o Servidor
    // retorna a resposta do Servidor ao pedido
    public String sendRequest(String request){
        try{
            this.out = new PrintWriter(socket.getOutputStream());
            this.out.println(request);
            this.out.flush();
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = this.in.readLine();
            System.out.println(response);
            return response;
        }
        catch(IOException e){
            System.out.println("[ClienteCon] Send> Request RIP !!!");
            System.out.println(e);
            return "FAIL";
        }
    }
    
    // Recebe Object User do Servidor
    public User receiveUser(){
        try{
            // conexao recebe o User mandado pelo servidor (versao Object)
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            try{
                this.user = (User)fromServer.readObject();
                System.out.println(user.toStringUser());
            }
            catch (ClassNotFoundException e){
                System.out.println("[ClienteCon] User class missing...");
            }
//            fromServer.close();
        }
        catch (IOException e) {
            System.out.println("[ClienteCon] Receive> User RIP !!!");
            System.out.println(e);
        }
        return this.user;
    }
    
}