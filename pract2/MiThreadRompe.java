package pract2;

public class MiThreadRompe extends Thread{
	
	private RompeEmpate rompeEmpate;
	private Entero n;
	private Integer id;

    public MiThreadRompe(RompeEmpate rompeEmpate, Entero ent, int id) {
        this.rompeEmpate = rompeEmpate;
        this.n = ent;
        this.id = id;
    }

    @Override
    public void run() {
    	for (int i = 0; i < 100000; i++) {
            rompeEmpate.entryCS(id);
            
            if (id == 1) {
            	n.increase();
            }
            else {
            	n.decrease();
            }
            
            rompeEmpate.exitCS(id);
        }
    }
    
}
