package cliente;

import java.io.Serializable;
import servidor.Server;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author KIKO
 */
public class User implements Serializable {
    private final ReentrantLock lockUser;
    private final String email;
    private final String password;
    private double debt;
    private final ArrayList<Server> servidoresAlocados; 
    
    /**
     * Instancia User com email e password, mas sem Servidores alocados
     * @param email
     * @param password
     */
    public User(String email, String password){
        this.lockUser = new ReentrantLock();
        this.email = email;
        this.password = password;
        this.servidoresAlocados = new ArrayList<>();
    }
    
    public User(User u)
    {
        this.lockUser = u.getLock();
        this.email = u.getEmail();
        this.password = u.getPassword();
        this.servidoresAlocados = u.getServidoresAlocados();
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
        double debt1;
        lock();
        debt1 = this.debt;
        unlock();
        return debt1;
    }
    public ArrayList<Server> getServidoresAlocados() {
        ArrayList<Server> servidoresA;
        lock();
        servidoresA = this.servidoresAlocados;
        unlock();
        return servidoresA;
    }
    
    public ReentrantLock getLock(){
        return this.lockUser;
    }

    // SETTERS
    public void setDebt(double debt) {
        lock();
        this.debt = debt;
        unlock();
    }
    
    
    public void addServer(Server server){
        lock();
        this.servidoresAlocados.add(server);
        unlock();
    }
    public void removeServer(Server server){
        lock();
        this.servidoresAlocados.remove(server);
        unlock();
    }
    
    public double getTotalPrice(){
        return this.servidoresAlocados.stream().mapToDouble(a -> a.getPrice())
                                       .sum();
    }

    public String toStringUser(){
        StringBuilder user = new StringBuilder();
        user.append(">Email: ").append(this.email).append("\n");
        user.append(" Password: ").append(this.password).append("\n");
        for(Server server : this.servidoresAlocados)
            user.append(server.toStringServer());
        return user.toString();
    }
    
    public User clone(){
        return new User(this);
    }
    
}
