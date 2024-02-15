package pract2;

public class RompeEmpate {
	// Declaramos la variable como volatile para que la variable cuando cambie llame a todos los otros procesos
    private volatile Boolean in1 = false;
    private volatile Boolean in2 = false;
    private volatile int last;

	public void entryCS(int id) {
		// TODO Auto-generated method stub
		if (id == 1) {
			in1 = true;
			last = id;
			while (in2 && last == 1);
		}
		
		else {
			in2 = true;
			last = id;
			while (in1 && last == 2);
		}
	}



	public void exitCS(int id) {
		// TODO Auto-generated method stub
		if (id == 1) {
			in1 = false;
		}
		
		else {
			in2 = false;
		}
	}
	
	public static void main(String[] args) {
    	RompeEmpate rompeEmpate = new RompeEmpate();
        Entero n = new Entero();

        MiThreadRompe thread1 = new MiThreadRompe(rompeEmpate, n, 1);
        MiThreadRompe thread2 = new MiThreadRompe(rompeEmpate, n, 2);

        thread1.start();
        thread2.start();
        
        try {
        	thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        try {
        	thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(n.getn());
    }
}
