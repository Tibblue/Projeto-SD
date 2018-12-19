package main;
import cliente.Cliente;
import java.io.IOException;
import servidor.MainServidor;

import java.net.UnknownHostException;
import java.util.Scanner;
import servidor.BD;
import cliente.forms.LoginForm;
import cliente.Cliente;

// Main capaz de iniciar um AgenteUDP ou ReverseProxy
public class MainSD {
    public static void main(String[] args) {
        
        BD data = new BD();
        
        /*try{          PARA QUANDO TIVERMOS PERSISTENCIA
            data = data.load();
        } catch (IOException i){
            betess = betess.povoar();
            betess.save(betess);
        }*/
        //betess = betess.load(); Acho que não é preciso fazer este
        
        Scanner input = new Scanner(System.in);
        System.out.println("1 - Cliente");
        System.out.println("2 - Servidor");
        System.out.println("0 - Sair");
        int in = input.nextInt();
        try{
            switch(in){
                case 1:
                    LoginForm form = new LoginForm(data);
                    Cliente cliente = new Cliente();
                    form.setVisible(true);
                    cliente.run();
                    break;
                case 2:
                    MainServidor servidor = new MainServidor();
                    servidor.start();
                    break;
                default:
                    System.exit(0);
                    break;
            }
        } catch (IOException e) {
            System.out.println("<MAIN> IO ardeu");
            System.out.println(e);
        }
    }
}
