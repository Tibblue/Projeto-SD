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
    
    /**
     * Instancia User apartir de uma String com ta a informaçao do User
     * @param user
     */
    public User(String user){
        String[] userSplit = user.split(" ");
        String mail = userSplit[1];
        String pass = userSplit[2];
        ArrayList<Server> servers = new ArrayList<>();
        for(int i=3; i<userSplit.length; i++){
            String tipo = userSplit[i];
            String nome = userSplit[i+1];
            double price = Double.parseDouble(userSplit[i+2]);
            int reserva = Integer.parseInt(userSplit[i+3]);
            double lastBid = Double.parseDouble(userSplit[i+4]);
            boolean isLeilao = Boolean.parseBoolean(userSplit[i+5]);
            Server server = new Server(nome,tipo,price,reserva,lastBid,isLeilao);
            servers.add(server);
        }
        this.lockUser = new ReentrantLock();
        this.email = mail;
        this.password = pass;
        this.servidoresAlocados = servers;
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
        this.lockUser.lock();
        debt1 = this.debt;
        this.lockUser.unlock();
        return debt1;
    }
    public ArrayList<Server> getServidoresAlocados() {
        // TODO add locks
        return this.servidoresAlocados;
    }

    // SETTERS
    public void setDebt(double debt) {
        this.lockUser.lock();
        this.debt = debt;
        this.lockUser.unlock();
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

    /**
     * Retorna a lista de Users
     * Mesmo propósito da toString default, 
     * mas mais adequado para mensagens entre servidor e cliente
     * @return String
     */
    public String toStringUserToSend(){
        StringBuilder user = new StringBuilder();
        user.append("USER ");
        user.append(this.email).append(" ");
        user.append(this.password).append(" ");
        for(Server server : this.servidoresAlocados)
            user.append(server.toStringServerToSend());
        return user.toString();
    }
    
    /**
     * Retorna a lista de Users
     * Mesmo propósito da toString default, 
     * mas mais pretty
     * @return String
     */
    public String toStringUser(){
        StringBuilder user = new StringBuilder();
        user.append(">Email: ").append(this.email).append("\n");
        user.append(" Password: ").append(this.password).append("\n");
        for(Server server : this.servidoresAlocados)
            user.append(server.toStringServer());
        return user.toString();
    }
    
}
