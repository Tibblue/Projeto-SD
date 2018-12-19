package servidor;

import cliente.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author KIKO
 */
public class BaseDados {
    public final HashMap<String,String> usersOLD;
//    public final HashMap<String,User> users;
    public final HashMap<String,ArrayList<Server>> servidores;
    
    public BaseDados(){
        this.usersOLD = new HashMap<>();
        this.servidores = new HashMap<>();
        
        // USERS
        this.usersOLD.put("kiko@email.com", "kiko");
        this.usersOLD.put("camaz@email.com", "camaz");
        this.usersOLD.put("vitor@email.com", "vitor");
        
        // SERVERS
        // criar servidor
        Server batatas1 = new Server("batatas1","potato1.small",1.00);
        Server batatas2 = new Server("batatas2","potato1.medium",2.00);
        // criar ArrayList para cada tipo de server
        ArrayList<Server> potato1small = new ArrayList<>();
        ArrayList<Server> potato1medium = new ArrayList<>();
        // adicionar servidor ao ser ArrayList respetivo
        potato1small.add(batatas1);
        potato1medium.add(batatas2);
        // adicionar server a hash
        this.servidores.put("potato1.small", potato1small);
        this.servidores.put("potato1.medium", potato1medium);
    }
    
    /**
     * Retorna a lista de Users
     * @return String
     */
    public String toStringUsers(){
        StringBuilder str = new StringBuilder();
        str.append("#----------  Users  ----------#\n");
        for(String key : this.usersOLD.keySet()){
            str.append("Email: ").append(key).append(" / ");
            str.append("Password: ").append(this.usersOLD.get(key)).append("\n");
        }
        str.append("#----------  -----  ----------#\n");
        return str.toString();
    }
    
    /**
     * Retorna a lista de Servidores
     * @return String
     */
    public String toStringServidores(){
        StringBuilder str = new StringBuilder();
        str.append("#----- Servers -----#\n");
        for(String key : this.servidores.keySet()){
            str.append(" Tipo => ").append(key).append("\n");
            ArrayList<Server> array = this.servidores.get(key);
            for(Server server : array){
                str.append(" Nome -> ").append(server.getNome()).append("\n");
                str.append(" Preço -> ").append(server.getPrice()).append("\n");
                str.append(" Reserva -> ").append(server.getIdReserva()).append("\n");
            }
        }
        str.append("#----- ------- -----#\n");
        return str.toString();
    }
    
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
    
    public synchronized ArrayList<Server> getServersByType(String type)
    {
        return this.servidores.get(type);
    }    
    
    public synchronized List<Server> getFreeUsersByType(String type)
    {
        ArrayList<Server> servers = new ArrayList<>();
        
        servers = this.servidores.get(type);
        
        return servers.stream().filter(s -> s.getUsed() == false).collect(Collectors.toList());
    }
    
    public synchronized void resetAllServersOfType(String type)
    {
        List<Server> servers = this.servidores.get(type);
        
        servers.stream().forEach((s) -> {
            s.setIdReserva(0);
        });
    }
    
    
}
