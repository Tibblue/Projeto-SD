/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.*;
import java.net.Socket;
/**
 *
 * @author KIKO
 */
public class ServidorThread extends Thread {
    private final Socket clienteSocket;
    private final InputStream fromClient;
    private final OutputStream toClient;
    private byte[] mensagem;
    
    public ServidorThread(Socket cliente) throws IOException {
        this.clienteSocket = cliente;
        this.fromClient = cliente.getInputStream();
        this.toClient = cliente.getOutputStream();
        this.mensagem = new byte[1024];
    }
    
    public void run(){
        System.out.println("  [ServidorThread] Novo cliente apartir de " + 
                            clienteSocket.getInetAddress().getHostAddress());
        
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(fromClient));
            String request;
            while( null!=(request=br.readLine()) ){
                System.out.println("Message received:" + request);
            }
//            while( fromClient.read(mensagem, 0, 1024) != -1 ) {
////                String msg = new String(packetRecebido.getData(), packetRecebido.getOffset(), packetRecebido.getLength());
//                System.out.println("  [ServidorThread] Mensagem: ");
//                System.out.println(mensagem);
//                System.out.println("  [ServidorThread] End of message");
////                toServer.write(mensagem, 0, 1024);
////                toServer.flush();
////                System.out.println("   [ClientTCPReceiver] flushed");
//            }
        }
        catch (IOException e) { 
            System.out.println ("  [ServidorThread] ERRO: Falha de leitura!!!"); 
            System.out.println(e);
        }
        
        System.out.println("  [ServidorThread] Cliente fechou ligação");
    }
    
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
