package servidor;

import java.io.*;
import java.net.Socket;
/**
 *
 * @author KIKO
 */
public class MainServidorWorker extends Thread {
    private final Socket clienteSocket;
    private final InputStream fromClient;
    private final OutputStream toClient;
    private byte[] mensagem;
    
    public MainServidorWorker(Socket cliente) throws IOException {
        this.clienteSocket = cliente;
        this.fromClient = cliente.getInputStream();
        this.toClient = cliente.getOutputStream();
        this.mensagem = new byte[1024];
    }
    
    public void run(){
        try{
            try (BufferedReader in = new BufferedReader(new InputStreamReader(this.fromClient));
                 PrintWriter out = new PrintWriter(this.toClient)) {
                
                System.out.println("[Worker] Processando conexao");
                String msg;
                while ((msg = in.readLine()) != null && !msg.equals("exit")) {
                    System.out.println("[Cliente] " + msg);
                    out.println("ECHO "+msg);
                    out.flush();
                }
                
            }
            catch(IOException e){
                System.out.println("[Worker] IO ardeu !!!");
                System.out.println(e);
            }
            clienteSocket.close();
            System.out.println("[Worker] Terminando conexao");
        }
        catch(Exception e){
            System.out.println("[Worker] EXCEÇAO !!!");
            System.out.println(e);
        }
            
//        System.out.println("  [ServidorThread] Novo cliente apartir de " + 
//                            clienteSocket.getInetAddress().getHostAddress());
//        
//        try{
//            BufferedReader br = new BufferedReader(new InputStreamReader(fromClient));
//            String request;
//            while( null!=(request=br.readLine()) ){
//                System.out.println("Message received:" + request);
//            }
//            while( fromClient.read(mensagem, 0, 1024) != -1 ) {
////                String msg = new String(packetRecebido.getData(), packetRecebido.getOffset(), packetRecebido.getLength());
//                System.out.println("  [MainServidorWorker] Mensagem: ");
//                System.out.println(mensagem);
//                System.out.println("  [MainServidorWorker] End of message");
////                toServer.write(mensagem, 0, 1024);
////                toServer.flush();
////                System.out.println("   [ClientTCPReceiver] flushed");
//            }
//        }
//        catch (IOException e) { 
//            System.out.println ("  [ServidorThread] ERRO: Falha de leitura!!!"); 
//            System.out.println(e);
//        }
//        
//        System.out.println("  [ServidorThread] Cliente fechou ligação");
    }
    
    
}
