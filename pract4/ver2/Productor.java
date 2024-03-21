package pract4.ver2;

public class Productor extends Thread {
    private Almacen almacen;
    private int P;
    private int id;

    public Productor(Almacen almacen, int P, int id) {
        this.almacen = almacen;
        this.P = P;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < P; i++) {
            Producto producto = new Producto();
            almacen.almacenar(producto);
            System.out.println("Productor " + id + " ha almacenado " + producto.getProd() + " en la iteración " + i);
        }
    }
}