/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author KIKO
 */
public class Server {
    private String nome;
    private String tipo;
    private float precoHora;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getPrecoHora() {
        return precoHora;
    }

    public void setPrecoHora(float precoHora) {
        this.precoHora = precoHora;
    }
    
    public Server(String nome, String tipo, float precoHora){
        this.nome = nome;
        this.tipo = tipo;
        this.precoHora = precoHora;
    }

}
