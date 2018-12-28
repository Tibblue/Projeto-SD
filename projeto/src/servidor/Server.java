package servidor;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author KIKO
 */
public class Server implements Serializable {
    private final ReentrantLock lockServer;
    private final String nome;
    private final String tipo;
    private final double price;
    private int idReserva;
    private double lastBid;
    private boolean isLeilao;
    private String owner; // ?email do user que esta a usar?
    
    public Server() {
        this.lockServer = new ReentrantLock();
        this.nome = "";
        this.tipo = "";
        this.price = 0.0;
        this.idReserva = 999999;
        this.lastBid = 0.0;
        this.isLeilao = false;
    }
    
    public Server(String nome, String tipo, double price){
        this.lockServer = new ReentrantLock();
        this.nome = nome;
        this.tipo = tipo;
        this.price = price;
        this.idReserva = 0;
        this.lastBid = 0.0;
        this.isLeilao = false;
    }

    public Server(String nome, String tipo, double price, int idReserva, double lastBid, boolean isLeilao){
        this.lockServer = new ReentrantLock();
        this.nome = nome;
        this.tipo = tipo;
        this.price = price;
        this.idReserva = idReserva;
        this.lastBid = lastBid;
        this.isLeilao = isLeilao;
    }

    // LOCKS
    public void lock() {
        this.lockServer.lock();
    }
    public void unlock() {
        this.lockServer.unlock();
    }
    
    // GETS
    public String getNome() {
        return this.nome;
    }
    public String getTipo() {
        return this.tipo;
    }
    public double getPrice() {
        return this.price;
    }
    public double getLastBid(){
        double lastBid1;
        this.lockServer.lock();
        lastBid1 = this.lastBid;
        this.lockServer.unlock();
        return lastBid1;
    }
    public boolean getIsLeilao() {
        boolean leilao;
        this.lockServer.lock();
        leilao = this.isLeilao;
        this.lockServer.unlock();
        return leilao;
    }
    public int getIdReserva() {
        int reserva;
        this.lockServer.lock();
        reserva = this.idReserva;
        this.lockServer.unlock();
        return reserva;
    }
    // pelo id de id sabemos se estamos a usar o server ou nao
    public boolean getUsed() {
        int id;
        this.lockServer.lock();
        id = this.idReserva;
        this.lockServer.unlock();
        
        if(id==0) return false;
        else return true;
    }
    // TODO GETUSER by email
    
    // SETS
    public synchronized void setIsLeilao(boolean isLeilao) {
        this.lockServer.lock();
        this.isLeilao = isLeilao;
        this.lockServer.unlock();
    }
    public synchronized void setIdReserva(int idReserva) {
        this.lockServer.lock();
        this.idReserva = idReserva;
        this.lockServer.unlock();
    }
    public boolean setNewBid(double valor){
        if(valor>this.lastBid){
            this.lockServer.lock();
            this.lastBid=valor;
            this.lockServer.unlock();
            return true;
        }
        return false;
    }
    
    
    public synchronized void reserva(int id){
        // o id da id é calculado pelo BancoServers
        // aqui apenas se faz set dele
        this.setIdReserva(id);
    }

    public synchronized void reservaLeilao(int id){
        // o id da id é calculado pelo BancoServers
        // aqui apenas se faz set dele
        this.setIdReserva(id);
        this.setIsLeilao(true);
    }
    
    public synchronized void freeServer(){
        this.idReserva = 0;
    }
    
    /**
     * Retorna a lista de Servidores
     * Mesmo propósito da toString default, 
     * mas mais adequado para mensagens entre servidor e cliente
     * @return String
     */
    public String toStringServerToSend(){
        StringBuilder server = new StringBuilder();
        server.append("SERVER ");
        server.append(this.tipo).append(" ");
        server.append(this.nome).append(" ");
        server.append(this.price).append(" ");
        server.append(this.idReserva).append(" ");
        server.append(this.lastBid).append(" ");
        server.append(this.isLeilao).append(" ");
        return server.toString();
    }
    
    /**
     * Retorna a lista de Servidores
     * Mesmo propósito da toString default, 
     * mas mais pretty
     * @return String
     */
    public String toStringServer(){
        StringBuilder server = new StringBuilder();
        server.append("=>Tipo: ").append(this.tipo).append("\n");
        server.append("  Nome: ").append(this.nome).append("\n");
        server.append("  Preço: ").append(this.price).append("\n");
        server.append("  Reserva: ").append(this.idReserva).append("\n");
        server.append("  Last Bid: ").append(this.lastBid).append("\n");
        server.append("  Leilão? ").append(this.isLeilao).append("\n");
        return server.toString();
    }
    
}
