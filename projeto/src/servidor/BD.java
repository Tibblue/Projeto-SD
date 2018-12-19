package servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author KIKO
 */
public class BD {
    public final HashMap<String,String> users;
    public final HashMap<String,ArrayList<Server>> servidores;
    
    public BD(){
        this.users = new HashMap<>();
        this.servidores = new HashMap<>();
        
        // USERS
        this.users.put("kiko@email.com", "kiko");
        this.users.put("camaz@email.com", "camaz");
                
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
    
    public String listUsers(){
        StringBuilder str = new StringBuilder();
        str.append("#----------  Users  ----------#\n");
        this.users.keySet().stream().map((key) -> {
            str.append("Email: ").append(key);
            return key;
        }).forEach((key) -> {
            str.append(" / Password: ").append(this.users.get(key)).append("\n");
        });
        str.append("#----------  -----  ----------#\n");
        return str.toString();
    }
    
    public String listServidores(){
        StringBuilder str = new StringBuilder();
        str.append("#----- Servers -----#\n");
        for(String key : this.servidores.keySet()){
            str.append(" Tipo => ").append(key).append("\n");
            ArrayList<Server> array = this.servidores.get(key);
            array.stream().map((server) -> {
                str.append(" Nome -> ").append(server.getNome()).append("\n");
                return server;
            }).map((server) -> {
                str.append(" Preço -> ").append(server.getPrice()).append("\n");
                return server;
            }).forEach(new Consumer<Server>() {
                @Override
                public void accept(Server server) {
                    str.append(" Reserva -> ").append(server.getIdReserva()).append("\n");
                }
            });
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
    public static void saveServers(String nomeFicheiro, HashMap<String,String> users) throws FileNotFoundException
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
