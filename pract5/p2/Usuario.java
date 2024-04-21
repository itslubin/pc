package pract5.p2;

import java.util.List;

public class Usuario {
    private int id;
    private String nombreUsuario;
    private String direccionIP;
    private List<Informacion> informacionCompartida; // Lista de fichero a la que tiene acceso

    public Usuario(int id, String nombreUsuario, String direccionIP) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.direccionIP = direccionIP;
        this.informacionCompartida = new java.util.ArrayList<>();
    }
    
    // Nota: Añadir la lista de ficheros que tiene almacenado, estos unicamente estan en el proyecto, cuando lo pide
    // lo lee entero y lo manda al usuario que toca

    // Constructor, getters y setters
    public int getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getDireccionIP() {
        return direccionIP;
    }

    public List<Informacion> getInformacionCompartida() {
        return informacionCompartida;
    }

    public String toString() {
        return id + "," + nombreUsuario + "," + direccionIP + "," + informacionCompartida.size() + ";";
    }
    
    // TODO: Hay que leer de la BD y cambiar todos los datos??
    // public static Usuario parseInformacion(String informacion) {
    //     String[] info = informacion.split(";");
    //     for (String i : info) {
    //         String[] data = i.split(",");
    //         informacionCompartida.add(new Informacion(data[0], data[1]));
    //     }
    // }
}
