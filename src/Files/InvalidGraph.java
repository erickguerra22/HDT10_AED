package Files;

/**
 * Excepcion para indicar una expresion invalida.
 * 
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class InvalidGraph extends Exception {

	/**
	 * Metodo constructor.
	 * 
	 * @param message
	 */
	public InvalidGraph(String message) {
		super(message);
	}

	/**
	 * Metodo constructor Excepcion para indicar una expresion invalida.
	 */
	public InvalidGraph() {
		super("El grafo ingresado contiene vertices independientes.");
	}
}
