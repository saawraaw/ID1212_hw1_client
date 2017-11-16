/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangmanclient;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Sarah
 */
public class UserInterface {
    private final BlockingQueue<String> ToBePrinted ;
    private final BlockingQueue<String> ReceivedFromUser ;
    private final Scanner scanner = new Scanner(System.in);
    private boolean stop = false ; 
    
    public UserInterface (BlockingQueue<String> ToBePrinted 
            ,BlockingQueue ReceivedFromUser) {
        this.ToBePrinted = ToBePrinted ;
        this.ReceivedFromUser = ReceivedFromUser ;
        
    }
    
    public void start () {
        new Thread (new ScreenWriter()).start() ;
        new Thread (new ScreenReader()).start() ;
    }
    
    synchronized public void stop (){
        stop = true ;
    }
    
    
    private class ScreenWriter implements Runnable {
        @Override
        public void run () {
            try {
                while (true) {
                    String s = ToBePrinted.take() ;
                    System.out.println(s);
                    if (s.equals("Quiting Game..."))
                        break ;
                    
                }
                //System.out.println("Stopping ScreenWriter");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    private class ScreenReader implements Runnable {
        @Override
        public void run () {
            try {
                while (true) {
                    String s = scanner.nextLine() ;
                    ReceivedFromUser.put(s);
                    if (s.equals("quit game"))
                        break ;
                }
                //System.out.println("Stopping ScreenReaderd");
            } catch (InterruptedException e) {
                e.printStackTrace () ;
            }

        }
    }
    
 
    
}
