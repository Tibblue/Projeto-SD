package main;
import cliente.User;
import java.io.IOException;
import servidor.MainServidor;

import java.net.UnknownHostException;
import java.util.Scanner;
import servidor.BaseDados;
import cliente.forms.LoginForm;
import cliente.User;

// Main capaz de iniciar um User ou Servidor
public class MainSD {
    public static void main(String[] args) {
        
        BaseDados data = new BaseDados();
        
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
                    User cliente = new User();
                    form.setVisible(true);
                    cliente.run();
                    break;
                case 2:
                    MainServidor servidor = new MainServidor(data);
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
