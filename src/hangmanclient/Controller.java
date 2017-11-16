/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangmanclient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Sarah
 */
public class Controller {
    private final BlockingQueue<String> receivedFromServer = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> receivedFromUser = new LinkedBlockingQueue<>();
    private final TCPClient client = new TCPClient (receivedFromServer, receivedFromUser) ;
    private final UserInterface ui = new UserInterface (receivedFromServer, receivedFromUser) ;

    
    public Controller () {
        client.connect(); 
        while (!client.getConnected() && !client.getConnectionFailed()){  
        }
        if (client.getConnected()) {
            ui.start () ; 
        }
            
     
    
    }

    
}
