package servidor;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author KIKO
 */
public class Server {
    private final ReentrantLock lockServer;
    private final String nome;
    private final String tipo;
    private final double price;
    private double lastBid;
    private boolean isLeilao;
    private int idReserva;
    
    public Server(String nome, String tipo, double price){
        this.lockServer = new ReentrantLock();
        this.nome = nome;
        this.tipo = tipo;
        this.price = price;
        this.lastBid = 0.0;
        this.idReserva = 0;
    }

    public Server() {
        this.lockServer = new ReentrantLock();
        this.nome = "";
        this.tipo = "";
        this.price = 0.0;
        this.lastBid = 0.0;
        this.idReserva = 999999;
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
        return this.lastBid;
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
    
    public synchronized void resetServer()
    {
        this.idReserva = 0;
    }
}
