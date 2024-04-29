package pract5.p2.mensaje;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MensajeFichero extends Mensaje {
    private String filename;
    private byte[] content;

    public MensajeFichero(int id_from, int id_to, String path) throws IOException {
        super(id_from, id_to);

        Path p = Paths.get(path);
        filename = p.getFileName().toString();
        content = Files.readAllBytes(p);
    }

    public void saveAs(String path) throws IOException{
        path += filename;
        Files.write(Paths.get(path), content);
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public int getTipo() {
        return 8;
    }

}
