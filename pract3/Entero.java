package pract3;

public class Entero {
	// Declaramos la variable como volatile para que la variable cambie mas frecuentemente
    private volatile Integer n;
    
    public Entero() {
    	this.n = 0;
    }
    
    // Añadimos synchronized para que las operaciones esten sincronizadas y sean atomicas
    public void increase() {
        // Modificamos la variable compartida
        n++;
    }
    
    public void decrease() {
        // Modificamos la variable compartida
        n--;
    }
    
    public Integer getn() {
    	return n;
    }
    
}
