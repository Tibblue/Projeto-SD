package cliente;

import servidor.Server;
import java.util.ArrayList;

/**
 *
 * @author KIKO
 */
public class User {
    public static void main(String[] args){
        User cliente = new User();
        ClienteConnection connection = new ClienteConnection(1234);
        connection.connect();
    }
    
    public void run(){
        connection.connect();
    }
    
    private ClienteConnection connection;
    private String email;
    private String password; // será preciso???
    private ArrayList<Server> servidoresAlocados; 
    
    public User(){
        this.connection = new ClienteConnection(1234);
    }
    
    public User(String email, String password){
        this.connection = new ClienteConnection(1234);
        this.email = email;
        this.password = password;
    }
    
    public boolean login(String password){
        return true;
    }
    
    public double getPrice(){
        return this.servidoresAlocados.stream().mapToDouble(a -> a.getPrice())
                                       .sum();
    }

}
