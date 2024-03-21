package pract4.ver1;

public class Consumidor extends Thread {
    private Almacen almacen;
    private int C;

    private int id;

    public Consumidor(Almacen almacen, int C, int id) {
        this.almacen = almacen;
        this.C = C;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < C; i++) {
            Producto producto = almacen.extraer();
            System.out.println("Consumidor " + id + " ha extraido " + producto.getProd() + " en la iteración " + i);
        }
    }
}