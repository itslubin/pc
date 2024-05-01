package pract5.p2;

import java.util.concurrent.Semaphore;

public class LESemaphore implements LE{

    int nr = 0;
    int nw = 0;
    Semaphore e;
    Semaphore r;
    Semaphore w;
    int dr = 0;
    int dw = 0;

    public LESemaphore() {
        e = new Semaphore(1);
        r = new Semaphore(0);
        w = new Semaphore(0);
    }

	@Override
	public void request_read() {
		try {
			e.acquire();
	        if(nw > 0){
	            dr++;
	            e.release();
	            r.acquire();
	        }
	        nr++;
	        if(dr > 0){
	            dr--;
	            r.release();
	        } else {
	            e.release();
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void release_read() {
		try {
			e.acquire();
	        nr--;
	        if(nr == 0 && dw > 0){
	            dw--;
	            w.release();
	        } else {
	            e.release();
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void request_write() {
		try {
			e.acquire();
            if(nr > 0 || nw > 0){
                dw++;
                e.release();
                w.acquire();
            }
            nw++;
            e.release();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void release_write() {
		try {
			e.acquire();
            nw--;
            if(dw > 0){
	            dw--;
	            w.release();
            }
            else if(dr > 0){
                dr--;
                r.release();
            }
            else {
                e.release();
            }
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
