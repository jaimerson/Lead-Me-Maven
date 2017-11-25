/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

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
    
    public static void esperarThreads(Collection<Future> tasks){
        for (Future currTask : tasks) {
            try {
                currTask.get();
            } catch (Throwable thrown) {
                System.err.println(thrown.getLocalizedMessage());
            }
        }
    }
}
