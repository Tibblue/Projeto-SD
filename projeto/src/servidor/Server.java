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
    
    public Server(Server s)
    {
        this.lockServer = s.getLock();
        this.nome = s.getNome();
        this.tipo = s.getTipo();
        this.price = s.getPrice();
        this.idReserva = s.getIdReserva();
        this.lastBid = s.getLastBid();
        this.isLeilao = s.getIsLeilao();
    }

    // LOCKS
    public void lock() {
        this.lockServer.lock();
    }
    public void unlock() {
        this.lockServer.unlock();
    }
    
    // GETS
    public ReentrantLock getLock()
    {
        return this.lockServer;
    }
    
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
        lock();
        lastBid1 = this.lastBid;
        unlock();
        return lastBid1;
    }
    public boolean getIsLeilao() {
        boolean leilao;
        lock();
        leilao = this.isLeilao;
        unlock();
        return leilao;
    }
    public int getIdReserva() {
        int reserva;
        lock();
        reserva = this.idReserva;
        unlock();
        return reserva;
    }
    // pelo id de id sabemos se estamos a usar o server ou nao
    public boolean getUsed() {
        int id;
        boolean ret = false;
        lock();
        id = this.idReserva;
        if(id!=0) ret = true;
        
        unlock();
        return ret;
        
    }
    // TODO GETUSER by email
    
    // SETS
    public synchronized void setIsLeilao(boolean isLeilao) {
        lock();
        this.isLeilao = isLeilao;
        unlock();
    }
    public synchronized void setIdReserva(int idReserva) {
        lock();
        this.idReserva = idReserva;
        unlock();
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
        lock();
        this.setIdReserva(id);
        unlock();
    }

    public synchronized void reservaLeilao(int id){
        // o id da id é calculado pelo BancoServers
        // aqui apenas se faz set dele
        lock();
        this.setIdReserva(id);
        this.setIsLeilao(true);
        unlock();
    }
    
    public synchronized void freeServer(){
        lock();
        this.idReserva = 0;
        unlock();
    }
    
    public Server clone()
    {
        return new Server(this);
    }

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
