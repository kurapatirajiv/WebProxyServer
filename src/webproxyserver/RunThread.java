/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webproxyserver;


import java.net.Socket;


/**
 *
 * @author Rajiv K
 */
public class RunThread implements Runnable {

    private final Socket client;

    public void run() {
     // To differentiate between two threads and improve readability
        for (int i = 0 ; i < 110;i++){
            System.out.print("*");
        }
        System.out.println("");
        ProxyCache.handle(client);
    }

    public RunThread(Socket client) {

        this.client = client;


    }
}
