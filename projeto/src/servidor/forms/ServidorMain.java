/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.forms;

import java.io.IOException;
import servidor.MainServidor;

/**
 *
 * @author KIKO
 */
public class ServidorMain {
    private ServidorForm form;
    private MainServidor servidor;
    
    public ServidorMain() throws IOException {
        this.form = new ServidorForm();
        this.servidor = new MainServidor();
    }
    
    public void run(){
        this.form.setVisible(true);
        this.servidor.run();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            ServidorMain servidor = new ServidorMain();
            servidor.run();
        }
        catch(IOException e){
            System.out.println("Nao foi possivel iniciar o servidor" + e);
        }
    }
    
}
