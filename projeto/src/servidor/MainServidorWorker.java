package servidor;

import cliente.User;
import java.io.*;
import java.net.Socket;
import java.util.List;
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
                    String request;
                    String response;
                    while ((request = in.readLine()) != null && !request.equals("LOGOUT")) {
                        System.out.println("[Cliente] request> " + request);
                        response = this.parse(request);
                        System.out.println("[Cliente] response> " + response);
                        out.println(response);
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
    
    private String parse(String request){
        String response = "";
        
        String[] loginSplit = request.split(" ");
        String requestType = loginSplit[0];
        String email = loginSplit[1];
        switch(requestType){
            case "BUY":
                String tipo = loginSplit[2];
                List<Server> free = bd.getFreeServersByType(tipo);
                if(free.size()>0){
                    Server server = free.get(0);
                    server.lock();
                    if(server.getUsed()){
                        // servidor foi  alocado entretanto...
                        // retorna falha no pedido
                        response = "FAIL SERVER_UNAVAILABLE";
                    }
                    else{
                        int idReserva = bd.nextIdReserva();
                        // atualizar o server
                        server.reserva(idReserva);
                        // adicionar o server ao user
                        User user = bd.getUser(email);
                        user.addServer(server);
                        bd.setUser(user);
                        // response
                        response = "SUCCESS ID " + idReserva;
                    }
                    server.unlock();
                }
                else response = "FAIL OUT_OF_SERVERS_of_type " + tipo;
                break;
            case "BID":
                break;
            case "INFO":
                break;
            default: response = "FAIL UNKNOWN_REQUEST";
                break;
        }
        return response;
    }
}
