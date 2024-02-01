package pract1;

class SharedResource {
    // Declaramos la variable como volatile para que la variable cambie mas frecuentemente
    private volatile Integer n = 0;
    
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
    
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        Integer N = 4;
        Thread[] lista = new Thread[N];
        
        for (int i = 0; i < N/2; ++i) {
        	lista[i] = new Thread(() -> {
                sharedResource.increase();
            });
        	lista[i].start();
        }
        
        for (int i = N/2; i < N; ++i) {
        	lista[i] = new Thread(() -> {
                sharedResource.decrease();
            });
        	lista[i].start();
        }
        
        // Esperar a que todos los threads terminen utilizando join
        for (int i = 0; i < N; i++) {
            try {
                lista[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println(sharedResource.getn());
    }
}
