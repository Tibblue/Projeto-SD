package servidor;

import cliente.User;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 *
 * @author KIKO
 */
public class BaseDados {
    private int lastIdReserva;
    private final HashMap<String,User> users;
    private final HashMap<String,ArrayList<Server>> servers;
//    private final HashMap<String,ArrayList<Server>> reservas;
    
    public BaseDados(){
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
    
    // LOCK AND UNLOCK METHODS /////////////////////////////////////////////////
    public void lockAllServers()
    {
        for (ArrayList<Server> a : this.servers.values()) {
            a.stream().forEach((s) -> {
                s.lock();
            });
        }
    }
    public void unlockAllServers()
    {
        for (ArrayList<Server> a : this.servers.values()) {
            a.stream().forEach((s) -> {
                s.unlock();
            });
        }
    }
    public void lockAllUsers()
    {
        this.users.values().stream().forEach((u) -> {
            u.lock();
        });
    }
    public void unlockAllUsers()
    {
        this.users.values().stream().forEach((u) -> {
            u.unlock();
        });
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GETTERS
    public synchronized HashMap<String,User> getAllUsers(){
        lockAllUsers();
        HashMap<String,User> usrs = this.users;
        unlockAllUsers();
        return usrs;
    }
    public synchronized HashMap<String,ArrayList<Server>> getAllServers(){
        lockAllServers(); 
        HashMap<String, ArrayList<Server>> servs = this.servers;
        unlockAllServers();
        return servs;
    }
    public synchronized User getUser(String email){
        lockAllUsers();
        User u = this.users.get(email);
        unlockAllUsers();
        return u;
    }
    public synchronized ArrayList<Server> getServersByType(String type){
        lockAllServers();
        ArrayList<Server> lista = this.servers.get(type);
        unlockAllServers();
        return lista;
    }
    
    // SETTERS
    public synchronized void setUser(User user){
        lockAllServers();
        this.users.put(user.getEmail(),user);
        unlockAllServers();
    }
    public synchronized void setServersByType(Server server){
        lockAllServers();
        ArrayList<Server> list = this.servers.get(server.getTipo());
        list.add(server);
        this.servers.put(server.getTipo(),list);
        unlockAllServers();
    }
    
    
    // unica funçao que vai tocar no nextIdReserva por isso synchronized chega
    public synchronized int nextIdReserva() {
        this.lastIdReserva++; // auto incrementaçao
        return lastIdReserva;
    }
    
    public synchronized void updateUserServer(String email, Server server){
        User user = this.users.get(email);
        user.addServer(server);
        this.users.put(email, user);
    }
    
    
    public synchronized void newBid(String tipo, double bid)
    {
        lockAllServers();
        Server s = getFreeServersByType(tipo).get(1);
        if(s.getLastBid() < bid){
            s.setNewBid(bid);
            s.setIsLeilao(true);
            s.setIdReserva(nextIdReserva());
        }
        unlockAllServers();
    }
    
    public synchronized void coverBid(int idReserva, double bid)
    {
        lockAllServers();
        for(ArrayList<Server> sv : this.servers.values()){
            for(Server s : sv){
                if(s.getIdReserva() == idReserva){
                    if(s.getLastBid() < bid){
                        s.setNewBid(bid);
                        s.setIsLeilao(true);
                        s.setIdReserva(nextIdReserva());
                    }
                }
            }
        }
        unlockAllServers();
    }
        
    public synchronized HashMap<String,ArrayList<Server>> getDemandableServers(){
        // LOCK ALL SERVERS 
        lockAllServers();
        
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
        // UNLOCK ALL SERVERS
        unlockAllServers();
        
        return lista;
    }
    
    // TODO? fazer outra funcao que so devolve o mais barato pra cada tipo
    public synchronized HashMap<String,ArrayList<Server>> getBidableServers(){
        // LOCK ALL SERVERS 
        lockAllServers();
        
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
        // UNLOCK ALL SERVERS
        unlockAllServers();
        
        return lista;
    }
    
    // Como apenas um cliente acede ao servidor em questão, não há necessidade de dar lock
    // Rever para a questão dos leilões
    public synchronized void freeServer(String email, int idReserva){
        // remover do User
        // atm esta a estourar com nullpointerException
//        for(Server s : this.users.get(email).getServidoresAlocados()){
//            if( idReserva==s.getIdReserva() ){
//                this.users.get(email).removeServer(s);
//            }
//        }
        // atualizar na lista de Servers
        for(ArrayList<Server> s : this.servers.values()){
            s.stream().filter((a) -> (a.getIdReserva() == idReserva)).map((a) -> {
                a.setIdReserva(0);
                a.setIsLeilao(false);
                return a;
            });
        }
    }
    
    // KIKO DEI LOCK AQUI TAMBÉM PORQUE ELE ESTAVA A VER SE OS SERVIDORES ESTÃO A SER USADOS (BETTER)
    public synchronized List<Server> getFreeServersByType(String type)
    {
        lockAllServers();
        ArrayList<Server> se = this.servers.get(type);
        List<Server> aux;
        
        aux = se.stream().filter(s -> s.getUsed() == false).collect(Collectors.toList());
        
        unlockAllServers();
        return aux;
    }
    
//    public synchronized void resetAllServersOfType(String type)
//    {
//        List<Server> servers = this.servidores.get(type);
//        servers.stream().forEach((s) -> {
//            s.setIdReserva(0);
//        });
//    }
        
    
    
    // Users
    public static void saveUsers(String nomeFicheiro, HashMap<String,String> users) throws FileNotFoundException{
       try{
           File file = new File(nomeFicheiro);
           FileOutputStream fos = new FileOutputStream(file);
           ObjectOutputStream oos = new ObjectOutputStream(fos);
           
           oos.writeObject(users);
           oos.flush();
           oos.close();
           fos.close();
       }
       catch(Exception e){
       }        
    }
    
    public static HashMap<String,String> loadUsers(String nomeFicheiro) throws FileNotFoundException{
       HashMap<String,String> users = new HashMap<>();
       try{
           File toRead = new File(nomeFicheiro);
            try (FileInputStream fis = new FileInputStream(toRead); 
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                
                users = (HashMap<String,String>) ois.readObject();
                
            }
       }
       catch(IOException | ClassNotFoundException e){
       } 
       return users;
    }
    
    // Servers
    public static void saveServers(String nomeFicheiro, HashMap<String,String> servers) throws FileNotFoundException{
       try{
           File file = new File(nomeFicheiro);
           FileOutputStream fos = new FileOutputStream(file);
           ObjectOutputStream oos = new ObjectOutputStream(fos);
           
           oos.writeObject(servers);
           oos.flush();
           oos.close();
           fos.close();
       }
       catch(Exception e){
       }        
    }
    
    public static HashMap<String,ArrayList<Server>> loadServers(String nomeFicheiro) throws FileNotFoundException{
       HashMap<String,ArrayList<Server>> servers = new HashMap<>();
       try{
           File toRead = new File(nomeFicheiro);
            try (FileInputStream fis = new FileInputStream(toRead); 
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                servers = (HashMap<String,ArrayList<Server>>) ois.readObject();
            }
       }
       catch(IOException | ClassNotFoundException e){
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
