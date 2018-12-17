package servidor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author KIKO
 */
public class BancoServers {
    private HashMap<String,ArrayList<Server>> servidores;
    
    public BancoServers(){
        this.servidores = new HashMap<>();
    }
    
    // funcao que vai carregar os servidores por nós definidos para a hash
    public void load(){
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
    
    
    
}
