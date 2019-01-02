package servidor;

import cliente.User;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author KIKO
 */
public class MainServidorWorker extends Thread {
    private final Socket clienteSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private ObjectOutputStream toClient;
    private final BaseDados bd;
    private String email;

    public MainServidorWorker(Socket cliente, BaseDados db) throws IOException {
        this.clienteSocket = cliente;
        this.in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
        this.out = new PrintWriter(clienteSocket.getOutputStream());
        this.bd = db;
    }

    @Override
    public void run(){
        try{
            // receber a autenticaçao
            if( this.autenticacao() ){
                // Worker comeca a processar a conexao
                System.out.println("[Worker] Login OK - Processando conexao");
                String request;
                String response;
                // Worker espera pedidos do cliente
                while ((request = in.readLine()) != null && !request.equals("LOGOUT")) {
                    // TODO melhorar parse
                    response = this.parse(request);
                    System.out.println("[Cliente] request> " + request);
                    System.out.println("[Cliente] response> " + response);
//                    out.println(response);
//                    out.flush();
                }
                System.out.println("[Worker] LOGOUT - Terminando conexao");
            }
            clienteSocket.close();
        }
        catch(IOException e){
            System.out.println("[Worker] EXCEPTION - Ligação terminada pelo User!!!");
            System.out.println(e);
        }
    }

    // faz autenticaçao de um User
    // todo podemos alterar de bool para String e retorna o erro ao cliente
    private boolean autenticacao(){
        try{
            // Recebe o pedido de LOGIN
            String login = in.readLine();
            System.out.println("[Worker] Autenticacao> " + login);
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
                // devolve confirmação do sucesso de autenticacao
                out.println("SUCCESS");
                out.flush();
                // envia o User e Servers para o Cliente
                this.sendUserAndServers();
                return true;
            }
            // devolve confirmação do falha de autenticacao
            out.println("FAIL");
            out.flush();
            return false;
        }
        catch(IOException e){
            System.out.println("[Worker] Autenticação - RIP in.readLine() !!!");
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
        String[] requestSplit = request.split(" ");
        String requestType = requestSplit[0];
        switch(requestType){
            case "BUY": // pedido de compra de um Server
                String tipo = requestSplit[2];
                this.demand(tipo);
                break;
            case "BID": // pedido de licitacao de um Server
                // TODO double check this
                String tipoS = requestSplit[2];
                double bid = new Double(requestSplit[3]);
                this.bid(tipoS,bid);
                break;
            case "REM": // pedido de libertação de um Server do User
                int id = Integer.parseInt(requestSplit[2]);
                this.free(email, id);
                break;
            case "GET_USER_SERVERS": // pedido do User Object e da Lista de Servers
                this.get();
                break;
            default: response = "FAIL UNKNOWN_REQUEST";
                break;
        }
        return response;
    }
    // Parse helpers ////////////
    private void demand(String tipo){
        String response;
        List<Server> free = bd.getFreeServersByType(tipo);
        if(free.size()>0){
            Server server = free.get(0);
            server.lock();
            if(server.getUsed()){
                // potencialmente o servidor foi alocado entretanto...
                response = "FAIL SERVER_UNAVAILABLE";
            }
            else{
                int idReserva = bd.demand(email, server);
                response = "SUCCESS ID " + idReserva;
            }
            server.unlock();
        }
        else 
            response = "FAIL OUT_OF_SERVERS_of_type " + tipo;
        out.println(response);
        out.flush();
        System.out.println(bd.getUser(email).toStringUser());
    }
    private void bid(String tipo, double bid){
        String response;
        
        List<Server> freeServers = bd.getFreeServersByType(tipo);
        if(freeServers.size()>0){
            // selecionar o mais barato TODO
            Server server = freeServers.get(0);

            server.lock();
            if(server.getUsed() && server.getLastBid() < bid){
                // potencialmente o servidor foi alocado entretanto...
                response = "FAIL SERVER_UNAVAILABLE";
            }
            else{
                int idReserva = bd.bid(email, server, bid);
                response = "SUCCESS ID " + idReserva;
            }
            server.unlock();
        }
        else response = "FAIL OUT_OF_SERVERS_of_type " + tipo;
            out.println(response);
            out.flush();
        System.out.println(bd.getUser(email).toStringUser());
    }
    private void free(String email, int id){
        this.bd.freeServer(email,id);
        out.println("SUCCESS REM " + id);
        out.flush();
        System.out.println(bd.getUser(email).toStringUser());
    }
    private void get(){
        out.println("SUCCESS SENDING_USER_SERVERS");
        out.flush();
        this.sendUserAndServers();
        System.out.println(bd.getUser(email).toStringUser());
        System.out.println(bd.toStringServidores());
    }
    /////////////////////////////
    
    // Envia Object User e HashMap de Servers para o Client
    private void sendUserAndServers(){
        try{
            ArrayList<Object> list = new ArrayList<>();
            list.add(bd.getUser(email));
            list.add(bd.getAllServers());

            toClient = new ObjectOutputStream(clienteSocket.getOutputStream());
            toClient.writeObject(list);
            toClient.flush();
        }
        catch(IOException e){
            System.out.println("[Worker] ERRO no envio do USER & SERVERS !!!");
            System.out.println(e);
        }
    }

}
