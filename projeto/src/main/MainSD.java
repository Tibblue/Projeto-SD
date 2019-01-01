package main;
import java.io.IOException;
import servidor.MainServidor;

import java.util.Scanner;
import servidor.BaseDados;
import cliente.forms.LoginForm;

// Main capaz de iniciar um User ou Servidor
public class MainSD {
    public static void main(String[] args) {
        BaseDados bd = new BaseDados();

        /* PARA QUANDO TIVERMOS PERSISTENCIA
        // persistencia da BD
        try{
            data = data.load();
        }
        catch (IOException i){
            betess = betess.povoar();
            betess.save(betess);
        }
        //betess = betess.load(); Acho que não é preciso fazer este
        */

        Scanner input = new Scanner(System.in);
        System.out.println("1 - Cliente");
        System.out.println("2 - Servidor");
        System.out.println("0 - Sair");
        int in = input.nextInt();
        try{
            switch(in){
                case 1:
                    LoginForm login = new LoginForm();
                    login.setVisible(true);
                    break;
                case 2:
                    MainServidor servidor = new MainServidor(bd);
                    servidor.start();
//                    ServidorForm form = new ServidorForm(db);
//                    form.setVisible(true);
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
