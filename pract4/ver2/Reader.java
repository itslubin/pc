package pract4.ver2;

public class Reader extends Thread {
    private AlmacenLE almacen;
    private int C;

    private int id;

    public Reader(AlmacenLE almacen, int C, int id) {
        this.almacen = almacen;
        this.C = C;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < C; i++) {
            Producto producto = almacen.leer(i);
            if (producto == null)
                System.out.println("Lector " + id + " ha intentado leer un producto " + i + " que no había escrito");
            else
                System.out.println("Lector " + id + " ha leído " + producto.getProd() + " en la iteración " + i);
        }
    }
}