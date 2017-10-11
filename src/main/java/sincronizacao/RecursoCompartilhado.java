/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sincronizacao;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author rafao
 */
public class RecursoCompartilhado {
    private Lock lock;
    
    public RecursoCompartilhado(){
        lock = new ReentrantLock(true);
    }
    
    public void requisitarAcesso(){
        lock.lock();
    }
    
    public void liberarAcesso(){
        lock.unlock();
    }
}
