package cliente;

import servidor.Server;
import java.util.ArrayList;

/**
 *
 * @author KIKO
 */
public class Cliente {
    public static void main(String[] args){
        Cliente cliente = new Cliente();
        ClienteConnection connection = new ClienteConnection(1234);
        connection.connect();
    }
    
    public void run(){
        connection.connect();
    }
    
    private String email;
    private String password; // será preciso???
    private ArrayList<Server> servidores; 
    private ClienteConnection connection;
    
    public Cliente(){
        this.connection = new ClienteConnection(1234);
    }
    
    public Cliente(String email, String password){
        this.connection = new ClienteConnection(1234);
        this.email = email;
        this.password = password;
    }
    
    public void login(){ // probs nao é aqui ._. => move to server
        
    }
    
    public double getPrice(){
        return this.servidores.stream().mapToDouble(a -> a.getPrice())
                                       .sum();
    }

}
