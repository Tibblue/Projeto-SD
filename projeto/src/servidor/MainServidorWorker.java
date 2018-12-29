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
    private String email;
    
    public MainServidorWorker(Socket cliente, BaseDados db) throws IOException {
        this.clienteSocket = cliente;
        this.fromClient = cliente.getInputStream();
        this.toClient = cliente.getOutputStream();
        this.bd = db;
    }
    
    @Override
    public void run(){
        try ( BufferedReader in = new BufferedReader(new InputStreamReader(this.fromClient));
                PrintWriter out = new PrintWriter(this.toClient)) {
            // receber a autenticaçao
            if( this.autenticacao(in, out) ){
                // Worker comeca a processar a conexao
                System.out.println("[Worker] Login OK - Processando conexao");
                String request;
                String response;
                // Worker espera pedidos do cliente
                while ((request = in.readLine()) != null && !request.equals("LOGOUT")) {
                    System.out.println("[Cliente] request> " + request);
                    response = this.parse(request);
                    System.out.println("[Cliente] response> " + response);
                    out.println(response);
                    out.flush();
                    if( response.split(" ")[0].equals("SUCCESS") ){
                        
                    }
                    System.out.println(bd.getUser(email).toStringUser());
                }
                System.out.println("[Worker] Terminando conexao");
                return;
            }
            clienteSocket.close();
            return;
        }
        catch(IOException e){
            System.out.println("[Worker] IO ardeu !!!");
            System.out.println(e);
        }
        System.out.println("[Worker] EXCEPTION - Terminando conexao!!!");
    }
    
    // faz autenticaçao de um User
    // todo podemos alterar de bool para String e retorna o erro ao cliente
    private boolean autenticacao(BufferedReader in, PrintWriter out){
        try{
            // Recebe o pedido de LOGIN
            String login = in.readLine();
            System.out.println("[Worker] Tentativa de LOGIN => " + login);
            String[] loginSplit = login.split(" ");
            String tipo = loginSplit[0];
            String emailU = loginSplit[1];
            String password = loginSplit[2];
            // Verifica se tudo está correto
            if( !tipo.equals("LOGIN") ){
                System.out.println("[Worker] Not Login - Terminando conexao");
            }
            else if( !bd.getAllUsers().containsKey(emailU) ){
                System.out.println("[Worker] User nao existe - Terminando conexao");
            }
            else if( !bd.getAllUsers().get(emailU).getPassword().equals(password) ){
                System.out.println("[Worker] Password errada - Terminando conexao");
            }
            else { //SUCESSO
                // guarda o email do user
                this.email = emailU;
                // mensagem de confirmação do sucesso de autenticacao
                out.println("SUCCESS");
                out.flush();
                // envia o User e Servers para o Cliente
                this.sendUser();
                this.sendServers();
                return true;
            }
            out.println("FAIL");
            out.flush();
            return false;
        }
        catch(IOException e){
            System.out.println("[Worker] IO ardeu !!!");
            System.out.println(e);
            return false;
        }
    }
    
    /**
     * Parses a request sent by the Client
     * Return "SUCCESS [info]" in case of Success
     * Return "FAIL [info]" in case of FAIL
     * Info is adicional information provided by the Server to the Client
     * @param request String with a request from the Client
     * @return String with SUCCESS/FAIL of the request
     */
    private String parse(String request){
        String response = "";
        System.out.println("DEBUG: request ar parse " + request + "Class: MainServidorWorker, LINE: 113");
        String[] loginSplit = request.split(" ");
        String requestType = loginSplit[0];
        String email = loginSplit[1];
        switch(requestType){
            case "BUY": // pedido de compra de um Server
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
                        user.addServer(server.clone());
                        bd.setUser(user);
                        // response
                        response = "SUCCESS ID " + idReserva;
                    }
                    server.unlock();
                }
                else response = "FAIL OUT_OF_SERVERS_of_type " + tipo;
                break;
            case "BID": // pedido de licitacao de um Server
                // TODO FASTTTT
                break;
            case "REM": // pedido de libertação de um Server do User
                int id = Integer.parseInt(loginSplit[2]);
                this.bd.freeServer(email,id);
                // response
                response = "SUCCESS REM " + id;
                break;
            case "GET_USER": // pedido do User Object
                this.sendUser();
                break;
            case "GET_SERVERS": // pedido da Lista de Servers
                this.sendServers();
                break;
            default: response = "FAIL UNKNOWN_REQUEST";
                break;
        }
        return response;
    }
    
    // Envia Object User para o Client
    private void sendUser(){
        try{
            ObjectOutputStream outToClient = new ObjectOutputStream(toClient);
            // Send the User object 
            outToClient.writeObject(bd.getUser(email));   
//            outToClient.close();
        }
        catch(IOException e){
            System.out.println("[Worker] ERRO no envio do USER !!!");
            System.out.println(e);
        }
    }
    // Envia HashMap de Servers para o Client
    private void sendServers(){
        try{
            ObjectOutputStream outToClient = new ObjectOutputStream(toClient);
            // Send the HashMap Servers object 
            outToClient.writeObject(bd.getAllServers());   
//            outToClient.close();
        }
        catch(IOException e){
            System.out.println("[Worker] ERRO no envio dos SERVERS !!!");
            System.out.println(e);
        }
    }
    
}
