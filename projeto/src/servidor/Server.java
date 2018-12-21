package servidor;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author KIKO
 */
public class Server {
    private final ReentrantLock lockConta;
    private String nome;
    private final String tipo;
    private final double price;
    private double lastBid;
    private boolean isLeilao;
    private int idReserva;

    
    public Server(String nome, String tipo, double price){
        this.lockConta = new ReentrantLock();
        this.nome = nome;
        this.tipo = tipo;
        this.price = price;
        this.lastBid = 0.0;
        this.idReserva = 0;
    }

    public Server() {
        this.lockConta = new ReentrantLock();
        this.nome = "";
        this.tipo = "";
        this.price = 0.0;
        this.lastBid = 0.0;
        this.idReserva = 999999;
    }
    
    // LOCKS
    public void lock() {
        this.lockConta.lock();
    }
    public void unlock() {
        this.lockConta.unlock();
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
        this.lockConta.lock();
        leilao = this.isLeilao;
        this.lockConta.unlock();
        return leilao;
    }
    public int getIdReserva() {
        int reserva;
        this.lockConta.lock();
        reserva = this.idReserva;
        this.lockConta.unlock();
        return reserva;
    }
    // pelo id de id sabemos se estamos a usar o server ou nao
    public boolean getUsed() {
        int id;
        
        this.lockConta.lock();
        id = this.idReserva;
        this.lockConta.unlock();
        
        if(id==0) return false;
        else return true;
    }
    // TODO GETUSER by email
    
    // SETS
    public synchronized void setIsLeilao(boolean isLeilao) {
        this.lockConta.lock();
        this.isLeilao = isLeilao;
        this.lockConta.unlock();
    }
    public synchronized void setIdReserva(int idReserva) {
        this.lockConta.lock();
        this.idReserva = idReserva;
        this.lockConta.unlock();
    }
    public boolean setNewBid(double valor){
        if(valor>this.lastBid){
            this.lockConta.lock();
            this.lastBid=valor;
            this.lockConta.unlock();
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
