package pract4.ver1;

public class Writer extends Thread {
    private AlmacenLE almacen;
    private int P;
    private int id;

    public Writer(AlmacenLE almacen, int P, int id) {
        this.almacen = almacen;
        this.P = P;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < P; i++) {
            Producto producto = new Producto();
            almacen.escribir(producto, i);
            System.out.println("Escritor " + id + " ha escribido " + producto.getProd() + " en la iteración " + i);
        }
    }
}