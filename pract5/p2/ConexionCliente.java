package pract5.p2;

import java.net.Socket;

public class ConexionCliente {
    private Socket socket;
    private Usuario usuario;

    // Constructor, getters y setters
    public ConexionCliente(Socket socket, Usuario usuario) {
        this.socket = socket;
        this.usuario = usuario;
    }

    public Socket getSocket() {
        return socket;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
}