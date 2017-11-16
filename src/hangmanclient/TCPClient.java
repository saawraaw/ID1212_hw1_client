/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangmanclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Sarah
 */
public class TCPClient {
    Socket clientSocket ;
    BufferedReader fromServer ; 
    PrintWriter toServer ; 
    private final BlockingQueue<String> receivedValues ;
    private final BlockingQueue<String> toBeSentValues ;
    private boolean connectionFailed = false; 
    private boolean connected = false ; 
    private final int timeoutMillis = 20000 ;
    

    public TCPClient (BlockingQueue<String> receivedValues ,
            BlockingQueue<String> toBeSentValues) {
       this.receivedValues = receivedValues ;
       this.toBeSentValues = toBeSentValues ;
       
    }
    

    
    public void connect () {
        CompletableFuture.runAsync(() -> {
            try {
                clientSocket = new Socket ("localhost" , 3333);
                //clientSocket.setSoTimeout(timeoutMillis);
                fromServer = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                toServer = new PrintWriter(clientSocket.getOutputStream(),true);  
                new Thread(new Listener()).start();
                new Thread(new Sender()).start();
                System.out.println("Connected To Game Server\n"); 
                connected = true ;
            } catch (ConnectException e) {
                connectionFailed = true ;
                System.out.println ("Connection Failed\nPlease Run "
                        + "The Program Again");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e){
                
            }
        });
        
        
    }
    
    private void disconnect () {
        connected = false ;
        try {
            //System.out.println("Disconnecting");
            clientSocket.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    synchronized public boolean getConnectionFailed () {
        return connectionFailed ;
    }
    
    public boolean getConnected () {
        return connected ;
    }
    
 
    
    
    private class Listener implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    String str ;
                    if ((str = fromServer.readLine()) != null) {
                        receivedValues.put(str);
                        if (str.equals("Quiting Game..."))
                        break ;
                    }                
                }
                disconnect();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            
        } 
    }
    
    private class Sender implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    String s = toBeSentValues.take() ; 
                    toServer.println(s);
                    if (s.equals("quit game"))
                        break ;
                }   
            }  catch (InterruptedException e) {
                e.printStackTrace();
            }
           
        }  
    }
    
  
}
