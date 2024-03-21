package pract4.ver2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Entero {
	// Declaramos la variable como volatile para que la variable cambie mas frecuentemente
    private volatile Integer n;
    
    private Lock lock;
    
    public Entero() {
        lock = new ReentrantLock();
    	this.n = 0;
    }
    
    // Añadimos synchronized para que las operaciones esten sincronizadas y sean atomicas
    public void increase() {
        // Modificamos la variable compartida
        lock.lock();
        n++;
        lock.unlock();
    }
    
    public void decrease() {
        // Modificamos la variable compartida
        lock.lock();
        n--;
        lock.unlock();
    }
    
    public Integer getn() {
        lock.lock();
        int res = n;
        lock.unlock();
    	return res;
    }
    
}
