package cliente;

import servidor.Server;
import java.util.ArrayList;

/**
 *
 * @author KIKO
 */
public class User {
    private final String email;
    private final String password;
    private final ArrayList<Server> servidoresAlocados; 
    
    public User(String email, String password){
        this.email = email;
        this.password = password;
        this.servidoresAlocados = new ArrayList<>();
    }
    
    // GETTERS
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public ArrayList<Server> getServidoresAlocados() {
        // TODO add locks
        return this.servidoresAlocados;
    }
    
    
    public void addServer(Server server){
        // TODO add locks
        this.servidoresAlocados.add(server);
    }
    public void removeServer(Server server){
        // TODO add locks
        this.servidoresAlocados.remove(server);
    }
    
    public double getTotalPrice(){
        return this.servidoresAlocados.stream().mapToDouble(a -> a.getPrice())
                                       .sum();
    }

}
