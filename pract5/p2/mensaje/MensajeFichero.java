package pract5.p2.mensaje;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MensajeFichero extends Mensaje {
    private String filename;

    public MensajeFichero(int id_from, int id_to, String path) throws IOException {
        super(id_from, id_to);

        filename = path;
    }

    public void save(int c) throws IOException{
    	BufferedWriter outChars = null;
    	BufferedReader inChars = null;
    	try {
    		inChars = new BufferedReader(new FileReader(filename));
        	outChars = new BufferedWriter(new FileWriter("C" + c + filename));
        	
        	String line;
    	    while ((line = inChars.readLine()) != null) {
    	    	outChars.write(line); outChars.newLine();
    	    }
    	}
    	
    	finally {
    		if (inChars != null) {inChars.close();}
    		if (outChars != null) {outChars.close();}
    	}
        
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public int getTipo() {
        return 8;
    }

}
