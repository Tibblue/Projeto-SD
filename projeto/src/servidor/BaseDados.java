package servidor;

import cliente.User;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 *
 * @author KIKO
 */
public class BaseDados {
//    private final ReentrantLock lockBD;
    private int lastIdReserva;
    private final HashMap<String,User> users;
    private final HashMap<String,ArrayList<Server>> servers;
//    private final HashMap<String,ArrayList<Server>> reservas;
    
    public BaseDados(){
//        this.lockBD = new ReentrantLock();
        this.lastIdReserva = 0;
        this.users = new HashMap<>();
        this.servers = new HashMap<>();
        
        // USERS
        // criar user
        User debug = new User("kiko", "Password");
        User kiko = new User("kiko@email.com", "kiko");
        User vitor = new User("vitor@email.com", "vitor");
        User camaz = new User("camaz@email.com", "camaz");
        User raul = new User("raul@email.com", "raul");
        // inserir no hashmap
        this.users.put(debug.getEmail(), debug);
        this.users.put(kiko.getEmail(), kiko);
        this.users.put(vitor.getEmail(), vitor);
        this.users.put(camaz.getEmail(), camaz);
        this.users.put(raul.getEmail(), raul);
        
        // SERVERS
        // criar servidor
        Server batatas1 = new Server("batatas1","potato.small",1.00);
        Server batatas2 = new Server("batatas2","potato.small",1.00);
        Server batatas3 = new Server("batatas3","potato.medium",2.00);
        // criar ArrayList para cada tipo de server
        ArrayList<Server> potatosmall = new ArrayList<>();
        ArrayList<Server> potatomedium = new ArrayList<>();
        // adicionar servidor ao ser ArrayList respetivo
        potatosmall.add(batatas1);
        potatosmall.add(batatas2);
        potatomedium.add(batatas3);
        // adicionar server a hash
        this.servers.put("potato.small", potatosmall);
        this.servers.put("potato.medium", potatomedium);
    }
    
    // LOCKS
//    public void lock() {
//        this.lockBD.lock();
//    }
//    public void unlock() {
//        this.lockBD.unlock();
//    }
    
    // GETTERS
    public synchronized HashMap<String,User> getAllUsers(){
        return this.users;
    }
    public synchronized HashMap<String,ArrayList<Server>> getAllServers(){
        // TODO add locks
        return this.servers;
    }
    public synchronized User getUser(String email){
        // TODO add locks
        return this.users.get(email);
    }
    public synchronized ArrayList<Server> getServersByType(String type){
        // TODO add locks
        return this.servers.get(type);
    }
    
    //SETTERS
    public synchronized void setUser(User user){
        // TODO add locks
        this.users.put(user.getEmail(),user);
    }
    public synchronized void setServersByType(Server server){
        // TODO add locks
        ArrayList<Server> list = this.servers.get(server.getTipo());
        list.add(server);
        this.servers.put(server.getTipo(),list);
    }
    
    
    // unica funçao que vai tocar no nextIdReserva
    // por isso synchronized chega
    public synchronized int nextIdReserva() {
        this.lastIdReserva++; // auto incrementaçao
        return lastIdReserva;
    }
    
    public synchronized void updateUserServer(String email, Server server){
        User user = this.users.get(email);
        user.addServer(server);
        this.users.put(email, user);
    }
    
    public synchronized HashMap<String,ArrayList<Server>> getDemandableServers(){
        // TODO add locks
        HashMap<String,ArrayList<Server>> lista = new HashMap<>();
        for(String tipo : this.servers.keySet()){
            ArrayList<Server> aux = new ArrayList<>();
            for(Server server : this.servers.get(tipo)){
                if( !server.getUsed() || (server.getUsed() && server.getIsLeilao()) ){
                    aux.add(server);
                }
            }
            if(!aux.isEmpty()){
                lista.put(tipo, aux);
            }
        }
        return lista;
    }
    
    // TODO? fazer outra funcao que so devolve o mais barato pra cada tipo
    public synchronized HashMap<String,ArrayList<Server>> getBidableServers(){
        // TODO add locks
        HashMap<String,ArrayList<Server>> lista = new HashMap<>();
        for(String tipo : this.servers.keySet()){
            ArrayList<Server> aux = new ArrayList<>();
            for(Server server : this.servers.get(tipo)){
                // TODO double check este if qd nao tiver com sono
                if( !server.getUsed() || (server.getUsed() && server.getIsLeilao()) ){
                    aux.add(server);
                }
            }
            if(!aux.isEmpty()){
                lista.put(tipo, aux);
            }
        }
        return lista;
    }
    
    public synchronized void freeServer(int idReserva){
        
        for(ArrayList<Server> s : this.servers.values())
        {
            s.stream().filter((a) -> (a.getIdReserva() == idReserva)).map((a) -> {
                a.setIdReserva(0);
                return a;
            }).forEach((a) -> {
                a.setIsLeilao(false);
            });
        }
    }
    
    public synchronized List<Server> getFreeServersByType(String type)
    {
        ArrayList<Server> se = this.servers.get(type);
        return se.stream().filter(s -> s.getUsed() == false).collect(Collectors.toList());
    }
    
//    public synchronized void resetAllServersOfType(String type)
//    {
//        List<Server> servers = this.servidores.get(type);
//        servers.stream().forEach((s) -> {
//            s.setIdReserva(0);
//        });
//    }
    
    
    
    // Users
    public static void saveUsers(String nomeFicheiro, HashMap<String,String> users) throws FileNotFoundException
    {
       try
       {
           File file = new File(nomeFicheiro);
           FileOutputStream fos = new FileOutputStream(file);
           ObjectOutputStream oos = new ObjectOutputStream(fos);
           
           oos.writeObject(users);
           oos.flush();
           oos.close();
           fos.close();
       }
       catch(Exception e)
       {
       }        
    }
    
    public static HashMap<String,String> loadUsers(String nomeFicheiro) throws FileNotFoundException
    {   
       HashMap<String,String> users = new HashMap<>();
       try
       {
           File toRead = new File(nomeFicheiro);
            try (FileInputStream fis = new FileInputStream(toRead); 
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                
                users = (HashMap<String,String>) ois.readObject();
                
            }
       }
       catch(IOException | ClassNotFoundException e)
       {
       } 
       return users;
    }
    
    // Servers
    public static void saveServers(String nomeFicheiro, HashMap<String,String> servers) throws FileNotFoundException
    {
       try
       {
           File file = new File(nomeFicheiro);
           FileOutputStream fos = new FileOutputStream(file);
           ObjectOutputStream oos = new ObjectOutputStream(fos);
           
           oos.writeObject(servers);
           oos.flush();
           oos.close();
           fos.close();
       }
       catch(Exception e)
       {
       }        
    }
    
    public static HashMap<String,ArrayList<Server>> loadServers(String nomeFicheiro) throws FileNotFoundException
    {   
       HashMap<String,ArrayList<Server>> servers = new HashMap<>();
       try
       {
           File toRead = new File(nomeFicheiro);
            try (FileInputStream fis = new FileInputStream(toRead); 
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                
                servers = (HashMap<String,ArrayList<Server>>) ois.readObject();
                
            }
       }
       catch(IOException | ClassNotFoundException e)
       {
       } 
       return servers;
    }
    
    /**
     * Retorna a lista de Users
     * @return String
     */
    public String toStringUsers(){
        StringBuilder users = new StringBuilder();
        users.append("#----------  Users  ----------#\n");
        for(User user : this.users.values())
            users.append(user.toStringUser());
        users.append("#----------  -----  ----------#");
        return users.toString();
    }
    
    /**
     * Retorna a lista de Servidores
     * @return String
     */
    public String toStringServidores(){
        StringBuilder servers = new StringBuilder();
        servers.append("#---------- Servers ----------#\n");
        for(String key : this.servers.keySet()){
            for(Server server : this.servers.get(key))
                servers.append(server.toStringServer());
        }
        servers.append("#---------- ------- ----------#");
        return servers.toString();
    }
    
}
