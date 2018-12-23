package cliente;

import servidor.Server;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author KIKO
 */
public class User {
    private final ReentrantLock lockUser;
    private final String email;
    private final String password;
    private double debt;
    private final ArrayList<Server> servidoresAlocados; 
    
    public User(String email, String password){
        this.lockUser = new ReentrantLock();
        this.email = email;
        this.password = password;
        this.servidoresAlocados = new ArrayList<>();
    }
    
    // LOCKS
    public void lock() {
        this.lockUser.lock();
    }
    public void unlock() {
        this.lockUser.unlock();
    }
    
    // GETTERS
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public double getDebt() {
        // TODO add locks
        return debt;
    }
    public ArrayList<Server> getServidoresAlocados() {
        // TODO add locks
        return this.servidoresAlocados;
    }

    // SETTERS
    public void setDebt(double debt) {
        // TODO add locks
        this.debt = debt;
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
