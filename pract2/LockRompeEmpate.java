package pract2;

public class LockRompeEmpate implements Lock{
	
	private int N; // Numero de hilos
	private volatile int[] in;
	private volatile int[] last;
	
	public LockRompeEmpate(int N) {
		this.N = N;
		this.in = new int[N + 1];
		this.last = new int[N + 1];
	}
	
	@Override
	public void takeLock(int id) {
		// TODO Auto-generated method stub
		for (int j = 1; j <= N; j++) {
			in[id] = j;
			in = in; // para llamar a volatile
			last[j] = id;
			last = last;
			
			for (int k = 1; k <= N; k++) {
				if (k!= id) {
					while (in[k] >= in[id] && last[j] == id);
				}
			}
		}
	}

	@Override
	public void releaseLock(int id) {
		// TODO Auto-generated method stub
		in[id] = 0;
		in = in;
	}
	
}
