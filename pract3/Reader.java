package pract3;

public class Reader extends Thread {
    private AlmacenLB almacen;
    private int C;

    private int id;

    public Reader(AlmacenLB almacen, int C, int id) {
        this.almacen = almacen;
        this.C = C;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < C; i++) {
            Producto producto = almacen.leer(i);
            System.out.println("Consumidor " + id + " ha extraido " + producto.getProd() + " en la iteración " + i);
        }
    }
}