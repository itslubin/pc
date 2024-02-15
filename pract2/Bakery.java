package pract2;

public class Bakery {
	private volatile int turn1 = 0;
	private volatile int turn2 = 0;
	
	public void entryCS(int id) {
		// TODO Auto-generated method stub
		if (id == 1) {
			turn1 = 1;
			turn1 = turn2 + 1;
			while (turn2 != 0 && comp(turn1, 1, turn2, 2));
		}
		
		else {
			turn2 = 1;
			turn2 = turn1 + 1;
			while (turn1 != 0 && comp(turn2, 2, turn1, 1));
		}
	}



	private boolean comp(int a, int b, int c, int d) {
		return (a > c) || (a==c && b > d);
	}



	public void exitCS(int id) {
		// TODO Auto-generated method stub
		if (id == 1) {
			turn1 = 0;
		}
		
		else {
			turn2 = 0;
		}
	}
	
	public static void main(String[] args) {
		Bakery bakery = new Bakery();
        Entero n = new Entero();

        MiThreadBakery thread1 = new MiThreadBakery(bakery, n, 1);
        MiThreadBakery thread2 = new MiThreadBakery(bakery, n, 2);

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
