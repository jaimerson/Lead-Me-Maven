/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;

/**
 *
 * @author rafao
 */
public class ThreadUtil {
    
    public static void esperarThreads(List<Thread> threads){
        for (Thread t: threads){
            try {
                t.join();
            } catch (InterruptedException ex) {
                System.err.println("Erro ao esperar as threads");
            }
        }
    }
}
