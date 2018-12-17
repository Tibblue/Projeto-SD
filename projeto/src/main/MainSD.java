package main;
import cliente.Cliente;
import java.io.IOException;
import servidor.MainServidor;

import java.net.UnknownHostException;
import java.util.Scanner;

// Main capaz de iniciar um AgenteUDP ou ReverseProxy
public class MainSD {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("1 - Cliente");
        System.out.println("2 - Servidor");
        System.out.println("0 - Sair");
        int in = input.nextInt();
        try{
            switch(in){
                case 1:
                    Cliente cliente = new Cliente();
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
