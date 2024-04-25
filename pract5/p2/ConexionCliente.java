package pract5.p2;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ConexionCliente {
    private Socket socket;
    private Usuario usuario;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Constructor, getters y setters
    public ConexionCliente(Socket socket, Usuario usuario, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.usuario = usuario;
        this.in = in;
        this.out = out;
    }

    public Socket getSocket() {
        return socket;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

}