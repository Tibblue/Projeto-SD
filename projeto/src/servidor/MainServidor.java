package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author KIKO
 */
public class MainServidor extends Thread{
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final int PORT = 1234;
    private final ServerSocket serverSocket;
    private Socket clienteSocket;
    
    private final BaseDados bd;
     
    public MainServidor() throws IOException {
        this.serverSocket = new ServerSocket(this.PORT);
        this.bd = new BaseDados();
    }
    
    public MainServidor(BaseDados bd) throws IOException {
        this.serverSocket = new ServerSocket(this.PORT);
        this.bd = bd;
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
            // debuging prints
            System.out.println(this.bd.listUsers());
            System.out.println(this.bd.listServidores());
            
            System.out.println("[Servidor] Servidor à escuta na porta " + 
                                this.serverSocket.getLocalSocketAddress());
            try{
                // fica a escuta por pedidos para aceitar
                this.clienteSocket = this.serverSocket.accept();
            }
            catch(IOException e){
                System.out.println("[Servidor] Erro a aceitar ligação do cliente");
                System.out.println(e);
            }
            /*
            depois de o servidor aceitar a ligaçao o cliente tenta autenticar-se
            TODO zona de verificaçao de autenticaçao
            */
            try{
                // cria uma nova thread (worker) para a conexao
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
        catch(Exception e){
        }
    }
    
}
