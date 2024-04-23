package pract5.p2;

import java.io.Serializable;
import java.io.IOException;
import java.nio.file.Paths;

public class Informacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String path;
    // private byte[] contenido;

    public Informacion(String path) throws IOException {
        // this.contenido = Files.readAllBytes(Paths.get(path));
        this.nombre = Paths.get(path).getFileName().toString();
        this.path = path;
    }
    
    // public byte[] getContenido() {
    //     return contenido;
    // }

    public String getNombre() {
        return nombre;
    }

    public String getPath() {
        return path;
    }

    /*TODO: La información realmente se manda al usuario, o solo se actualiza la tabla del servidor??*/
    // public boolean saveAs(String path) {
    //     try {
    //         Files.write(Paths.get(path + "/" + nombre), contenido);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return false;
    //     }
    //     return true;
    // }

    public String toString() {
        return path + ";";
    }

    public static Informacion parseInformacion(String informacion) throws IOException {
        String[] info = informacion.split(";");
        return new Informacion(info[0]);
    }

}

