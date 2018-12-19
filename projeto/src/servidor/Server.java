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
    private boolean isLeilao;
    private int idReserva;

    // LOCKS
    public void lock() {
        this.lockConta.lock();
    }
    public void unlock() {
        this.lockConta.unlock();
    }
    
    // GETS
    public String getNome() {
        return nome;
    }
    public String getTipo() {
        return tipo;
    }
    public double getPrice() {
        return price;
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
       
    public Server(String nome, String tipo, double price){
        this.lockConta = new ReentrantLock();
        this.nome = nome;
        this.tipo = tipo;
        this.price = price;
        this.idReserva = 0;
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
