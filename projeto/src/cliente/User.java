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
    public ReentrantLock getLock()
    {
        return this.lockUser;
    }
    
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public double getDebt() {
        double debt1;
        lock();
        try {
            debt1 = this.debt;
        } finally {
            unlock();
        }
        return debt1;
    }
    public ArrayList<Server> getServidoresAlocados() {
        lock();
        ArrayList<Server> servidoresA = new ArrayList<>();
        try {    
            servidoresA = this.servidoresAlocados;
        } finally {
            unlock();
        }
        return servidoresA;
    }

    // SETTERS
    public void setDebt(double debt) {
        lock();
        this.debt = debt;
        unlock();
    }
    
    
    public void addServer(Server server){
        lock();
        try {
            this.servidoresAlocados.add(server);
        } finally {
            unlock();
        }
    }
    public void removeServer(Server server){
        lock();
        System.out.println("DEBUG: Reached removeServer, CLASS: User.java, LINE: 124");
        System.out.println("DEBUG: servAlocados initial size: " + this.servidoresAlocados.size());
        try {
            System.out.println("DEBUG: Before remove, CLASS: User.java, LINE: 126");
            this.servidoresAlocados.remove(server);
            System.out.println("DEBUG: After remove, CLASS: User.java, LINE: 128");
            System.out.println("DEBUG: servAlocados final size: " + this.servidoresAlocados.size());
        } finally {
            unlock();
        }
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
    
    public User clone()
    {
        return new User(this);
    }
    
}
