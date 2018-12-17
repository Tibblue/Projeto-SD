package cliente;

import servidor.Server;
import java.util.ArrayList;

/**
 *
 * @author KIKO
 */
public class Cliente {
    private String email;
    private String password; // será preciso???
    private ArrayList<Server> servidores; 
    
    public Cliente(){
    
    }
    
    public Cliente(String email, String password){
        this.email = email;
        this.password = password;
    }
    
    public void login(){ // probs nao é aqui ._. => move to server
        
    }
    
    public float getPrice()
    {
        return this.servidores.stream().map(a -> a.getPrice)
                                       .sum();
    }

    public static void main(String[] args){
        Cliente cliente = new Cliente();
    }
}
