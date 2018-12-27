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
                else if( !bd.getAllUsers().get(email).getPassword().equals(password) ){
                    System.out.println("[Worker] Password errada - Terminando conexao");
                }
                else{
                    // mensagem de confirmação do sucesso de autenticacao
                    out.println("SUCCESS");
                    out.flush();
                    // mensagem com a info do User
                    out.println(bd.getUser(email).toStringUserToSend());
                    out.flush();
                    // Worker comeca a processar a conexao normalmente
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
    }
    
}
