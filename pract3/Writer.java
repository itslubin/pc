package pract3;

public class Writer extends Thread {
    private AlmacenLB almacen;
    private int P;
    private int id;

    public Writer(AlmacenLB almacen, int P, int id) {
        this.almacen = almacen;
        this.P = P;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < P; i++) {
            Producto producto = new Producto();
            almacen.escribir(producto, i);
            System.out.println("Productor " + id + " ha almacenado " + producto.getProd() + " en la iteración " + i);
        }
    }
}