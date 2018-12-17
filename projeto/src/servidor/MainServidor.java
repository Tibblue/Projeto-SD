package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author KIKO
 */
public class MainServidor extends Thread{
    private AtomicBoolean running = new AtomicBoolean(true);
    private final int PORT = 1234;
    private final ServerSocket serverSocket;
    private Socket clienteSocket;
    
    private HashMap<String,String> bancoUsers;
    private final BD bancoServers;
     
    public MainServidor() throws IOException {
        this.serverSocket = new ServerSocket(this.PORT);
        this.bancoServers = new BD();
    }
    
    // Interrompe o MainServidor
    public void stopServidor(){
        this.running.set(false);
        this.interrupt();
        System.out.println("[Servidor] Servidor terminado !!!");
    }
    
    public void run() {
        System.out.println("[Servidor] Iniciando o Servidor");
        while(this.running.get()){
            String print;
            print = this.bancoServers.listUsers(); // debuging
            System.out.println(print);
            print = this.bancoServers.listServidores(); // debuging
            System.out.println(print);
            System.out.println("[Servidor] Servidor à escuta na porta " + 
                                this.serverSocket.getLocalSocketAddress());
            try{
                // fica a escuta por pedidos
                this.clienteSocket = this.serverSocket.accept();
            }
            catch(IOException e){
                System.out.println("[Servidor] Erro a aceitar ligação do cliente");
                System.out.println(e);
            }
            try{
                // cria uma nova thread para a conexao
                new MainServidorWorker(clienteSocket).run();
            }
            catch(IOException e){
                System.out.println("[Servidor] Erro a criar Thread para o cliente");
                System.out.println(e);
            }
            
        }
    }
    
    public static void main(String[] args){
        try{
            MainServidor servidor = new MainServidor();
            servidor.start();
        }
        catch(Exception e){}
        
    }
    
}
