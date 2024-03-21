package pract4.ver1;

public class AlmacenLE {
    private int N = 10;
    private Producto buf[];

    public AlmacenLE() {
        buf = new Producto[N];
    }

    public synchronized void escribir(Producto producto, int pos) {
        this.buf[pos] = producto;
    }

    public synchronized Producto leer(int pos) {
        Producto prod = buf[pos];

        return prod;
    }
}
