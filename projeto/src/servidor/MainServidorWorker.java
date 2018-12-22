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
    private final BaseDados bd;
    
    public MainServidorWorker(Socket cliente, BaseDados bd) throws IOException {
        this.clienteSocket = cliente;
        this.fromClient = cliente.getInputStream();
        this.toClient = cliente.getOutputStream();
        this.bd = bd;
    }
    
    @Override
    public void run(){
        try{
            try ( BufferedReader in = new BufferedReader(new InputStreamReader(this.fromClient));
                    PrintWriter out = new PrintWriter(this.toClient)) {
                // receber a autenticaçao
                String login = in.readLine();
                System.out.println("[Worker] Tentativa de LOGIN => " + login);
                String[] loginSplit = login.split(" ");
                String tipo = loginSplit[0];
                String email = loginSplit[1];
                String password = loginSplit[2];
                if( !tipo.equals("LOGIN") ){
                    System.out.println("[Worker] Not Login - Terminando conexao");
                }
                else if( !bd.getAllUsers().containsKey(email) ){
                    System.out.println("[Worker] User nao existe - Terminando conexao");
                }
                else if( !bd.getAllUsers().get(email).equals(password) ){
                    System.out.println("[Worker] Password errada - Terminando conexao");
                }
                else{
                    out.println("SUCCESS");
                    out.flush();
                    System.out.println("[Worker] Login OK - Processando conexao");
                    String msg;
                    while ((msg = in.readLine()) != null && !msg.equals("LOGOUT")) {
                        System.out.println("[Cliente] " + msg);
                        out.println("ECHO "+msg);
                        out.flush();
                    }
                    System.out.println("[Worker] Terminando conexao");
                    return;
                }
                out.println("FAIL");
                out.flush();
                clienteSocket.close();
                return;
            }
            catch(IOException e){
                System.out.println("[Worker] IO ardeu !!!");
                System.out.println(e);
            }
        }
        catch(Exception e){
            System.out.println("[Worker] EXCEÇAO !!!");
            System.out.println(e);
        }
        System.out.println("[Worker] EXCEPTION - Terminando conexao!!!");
        // zona com coisas para a LIGACAO TCP
//        System.out.println("  [ServidorThread] Novo cliente apartir de " + 
//                            clienteSocket.getInetAddress().getHostAddress());
//        try{
//            BufferedReader br = new BufferedReader(new InputStreamReader(fromClient));
//            String request;
//            while( null!=(request=br.readLine()) ){
//                System.out.println("Message received:" + request);
//            }
//            byte[] mensagem = new byte[1024];
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
//        System.out.println("  [ServidorThread] Cliente fechou ligação");
    }
    
}
