package cliente;

import java.util.ArrayList;
import servidor.Servidor;

/**
 *
 * @author KIKO
 */
public class Cliente {
    private String email;
    private String password; // será preciso???
    private ArrayList<Servidor> servidores; 
    
    public Cliente(){
    
    }
    
    public Cliente(String email, String password){
        this.email = email;
        this.password = password;
    }
    
    public void login(){ // probs nao é aqui ._. => move to server
        
    }
    
    public static void main(String[] args){
        Cliente cliente = new Cliente();
    }
}
