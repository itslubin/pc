package pract2;

public class MiThreadBakery  extends Thread{
	private Bakery bakery;
	private Entero n;
	private Integer id;

    public MiThreadBakery(Bakery bakery, Entero ent, int id) {
        this.bakery = bakery;
        this.n = ent;
        this.id = id;
    }

    @Override
    public void run() {
    	for (int i = 0; i < 100000; i++) {
    		bakery.entryCS(id);
            
            if (id == 1) {
            	n.increase();
            }
            else {
            	n.decrease();
            }
            
            bakery.exitCS(id);
        }
    }

}
