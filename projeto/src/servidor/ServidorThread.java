/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author KIKO
 */
public class ServidorThread extends Thread {
    Socket clienteSocket;
    
    public ServidorThread(Socket cliente){
        this.clienteSocket = cliente;
    }
    
    public void run(){
        System.out.println("batatas delivered");
    }
    
}
