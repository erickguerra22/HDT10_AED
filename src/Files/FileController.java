package Files;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase FileController.
 * @author Erick Guerra
 * @version 21/03/2022
 *
 */

public class FileController {
	
	public static final String PATH = System.getProperty("user.dir") + "\\guategrafo.txt";

	/**
	 * Metodo que se encarga de obtener todas las filas del archivo datos.txt
	 * @return String[]. Array con cada una de las filas de texto por casilla.
	 * @throws IOException
	 */
	public static String[] readFile() throws IOException {
		
		File doc = new File(PATH);

		  BufferedReader obj = new BufferedReader(new FileReader(doc));
		  ArrayList<String> linesList = new ArrayList<String>();

		  //leer y almacenar las filas del archivo de texto
		  String line;
		  while ((line = obj.readLine()) != null) {
		    linesList.add(line);
		  }
		  
		  obj.close();
		  
		  return linesList.toArray(new String[linesList.size()]); //convertir lista a array
	}
	
	/**
	 * Permite crear(si no existe) el archivo de almacenamiento y sobreescribir su contenido.
	 * @param text. Contenido del archivo
	 * @throws IOException
	 */
	public static void writeFile(String text, String fileName) throws IOException {
		File file = new File(PATH);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);

        fw.write(text);
        fw.close();        

    }
	
}