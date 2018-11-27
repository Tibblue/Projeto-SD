/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author KIKO
 */
public class Servidor extends Thread{
    private AtomicBoolean running = new AtomicBoolean(true);
    
    public final int PORT = 4100;
    private ServerSocket serverSocket;
    private Socket clienteSocket;
     
    public Servidor() throws IOException {
        this.serverSocket = new ServerSocket(this.PORT);
    }
    
    // Interrompe o Servidor
    public void stopServidor(){
        this.running.set(false);
        this.interrupt();
        System.out.println("[Servidor] Servidor terminado !!!");
    }
    
    public void run() {
        System.out.println("[Servidor] Iniciando o Servidor");
        while(this.running.get()){
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
                new ServidorThread(clienteSocket).run();
            }
            catch(IOException e){
                System.out.println("[Servidor] Erro a criar Thread para o cliente");
                System.out.println(e);
            }
            
        }
    }
    
}
