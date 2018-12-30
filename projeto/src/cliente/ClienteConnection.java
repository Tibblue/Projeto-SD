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
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private User user;
    private HashMap<String,ArrayList<Server>> bdServers;
    
    public ClienteConnection(){
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
            System.out.println("[ClienteCon] Connect>"+login);
            if( login.equals("SUCCESS") ){
                System.out.println("[ClienteCon] Login OK");
                return "SUCCESS";
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
            this.socket.close();
            System.out.println("[ClienteCon] Ligação terminada com sucesso!!!");
        } catch (IOException e) {
            System.out.println("[ClienteCon] CloseConnection RIP!!!");
            System.out.println(e);
        }
    }
    
    /**
     * Envia um pedido ao Servidor e recebe o SUCESSO/FAIL do pedido
     * @param request String with the request for the server
     * @return String with the result of the request
     */
    public String sendRequest(String request){
        try{
            System.out.println("[ClienteCon] Request> "+request);
            this.out = new PrintWriter(socket.getOutputStream());
            this.out.println(request);
            this.out.flush();
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = this.in.readLine();
            System.out.println("[ClienteCon] Response> "+response);
            return response;
        }
        catch(IOException e){
            System.out.println("[ClienteCon] SendRequest> Request RIP !!!");
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
                System.out.println("[ClienteCon] ReceiveUser> \n"+user.toStringUser());
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
    // Recebe HashMap de Servers do Servidor
    public HashMap<String,ArrayList<Server>> receiveServers(){
        try{
            // conexao recebe os Servers mandados pelo servidor (versao Object)
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            try{
                this.bdServers = (HashMap<String,ArrayList<Server>>)fromServer.readObject();
                System.out.print("[ClienteCon] ReceiveServers> \n");
                for(String key : this.bdServers.keySet()) 
                    for(Server server : this.bdServers.get(key))
                    System.out.print(server.toStringServer());
            }
            catch (ClassNotFoundException e){
                System.out.println("[ClienteCon] HashMap(...) class missing...");
            }
//            fromServer.close();
        }
        catch (IOException e) {
            System.out.println("[ClienteCon] Receive> Servers RIP !!!");
            System.out.println(e);
        }
        return this.bdServers;
    }
    
}