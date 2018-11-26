/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author KIKO
 */
public class Servidor extends Thread{
    public static final int PORT_NUMBER = 4100;
    private ServerSocket serverSocket;
    private Socket clienteSocket;
     
    public Servidor() throws IOException {
        this.serverSocket = new ServerSocket(this.PORT_NUMBER);
    }
     
    public void run() {
        System.out.println("BATATAS");
        while(true){
            try{this.clienteSocket = this.serverSocket.accept();}
            catch(IOException e){
                System.out.println("Erro a aceitar ligação do cliente\n" + e);
            }
            System.out.println("New client connected from " + 
                                clienteSocket.getInetAddress().getHostAddress());
            
            
        }

//        InputStream in = null;
//        try {
//            in = serverSocket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String request;
//            request = br.readLine();
//            System.out.println("Message received:" + request);
//        } catch (IOException e) {
//            System.out.println("Unable to get streams from client");
//        } finally {
//            try {
//                in.close();
//                serverSocket.close();
//                System.out.println("Socket closed");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
    }    
}
