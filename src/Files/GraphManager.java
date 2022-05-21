/**
 * 
 */
package Files;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Clase GraphManager. Gestiona las operaciones referentes al grafo de distancias entre las diferentes ciudades.
 * @author Erick Guerra, Diego Morales y Pablo Zamora.
 * @version 20/05/2022
 *
 */
public class GraphManager {
	private ArrayList<String[]> aristas = new ArrayList<String[]>();
	private HashMap<String, String[]> rutas;
	private ArrayList<String> vertices = new ArrayList<String>();
	private String graphCenter = "";
	
	/**
	 * Se encarga de extraer los datos del archivo y los almacena en un formato determinado.
	 * @param lines Lineas del archivo de texto.
	 * @throws InvalidGraph
	 */
	public void fileToGraph(String[] lines) throws InvalidGraph {
		for(String l : lines) {
			String[] line = l.split(" ");
			String[] inverted = l.split(" ");
			String origen = inverted[0];
			inverted[0] = inverted[1];
			inverted[1] = origen;
			if(!aristas.contains(line)) {
				aristas.add(line);
				aristas.add(inverted);
			}
			if(!vertices.contains(line[1]))
				vertices.add(line[1]);
			if(!vertices.contains(line[0]))
				vertices.add(line[0]);
		}
		matrizAdyacencias();
	}
	
	/**
	 * Se encarga de crear la matriz de adyacencia a partir de los vertices y aristas del grafo.
	 * @throws InvalidGraph
	 */
	private void matrizAdyacencias() throws InvalidGraph {
		Double[][] pesos = new Double[vertices.size()][vertices.size()];
		for(int i =0; i<vertices.size();i++) {
			int adyacencias = 0;
			for(int j=0;j<vertices.size();j++) {
				if(i==j)
					pesos[i][j] = 0.00;
				else {
					boolean foundAdy = false;
					for(String[] a : aristas) {
						if(a[0].equals(vertices.get(i))&&a[1].equals(vertices.get(j))) {
							pesos[i][j] = Double.parseDouble(a[2]);
							foundAdy = true;
							adyacencias++;
						}
					}
					if(!foundAdy)
						pesos[i][j] = Double.POSITIVE_INFINITY;
				}
			}
			if(adyacencias<1)
				throw new InvalidGraph();
		}
		floyd(pesos);
	}
	
	/**
	 * Se encarga de ejecutar el algoritmo de floyd para calcular la distancia m�s corta entre dos nodos.
	 * @param pesos. Matriz con los pesos de cada una de las aristas.
	 * @throws InvalidGraph
	 */
	private void floyd(Double[][] pesos) throws InvalidGraph{
		rutas=new HashMap<String, String[]>();
		ArrayList<String> ruta = new ArrayList<String>();
		for(int i=0;i<vertices.size();i++) {
			for(int j=0;j<vertices.size();j++) {
				if(i==j) {
					ruta = new ArrayList<String>();
					ruta.add("0");
					rutas.put(vertices.get(j)+", "+vertices.get(i), ruta.toArray(new String[ruta.size()]));
				}
				else {
					for(int k=0;k<vertices.size();k++) {
						if(k!=i && k!=j) {
							ruta = new ArrayList<String>();
							String viaje = vertices.get(j) + ", "+vertices.get(k);
							double newRoute = pesos[j][i]+pesos[i][k];
							if(newRoute<pesos[j][k]) {
								pesos[j][k]=newRoute;
								ruta.add(((Double)newRoute).toString());
								getIntermediateCities(ruta, vertices.get(j)+", "+vertices.get(i));
								ruta.add(vertices.get(i));
							}else {
								if(!rutas.containsKey(viaje))
									ruta.add(((Double)pesos[j][k]).toString());
							}
							if(ruta.size()>0)
								rutas.put(viaje, ruta.toArray(new String[ruta.size()]));
						}
					}
				}
			}
		}
		if(!validGraph(pesos))
			throw new InvalidGraph();
		graphCenter(pesos);
	}
	
	/**
	 * Se encarga de determinar si los datos almacenados corresponden al formato de un grafo valido.
	 * @param matriz 
	 * @return
	 */
	private boolean validGraph(Double[][] matriz) {
		for(Double[] d : matriz) {
			if(Arrays.asList(d).contains(Double.POSITIVE_INFINITY))
				return false;
		}
		return true;
	}
	
	/**
	 * Permite determinar la ciudad intermedia del grafo.
	 * @param ruta Almacena las diferentes rutas entre ciudades.
	 * @param key Identificador de la ruta.
	 */
	private void getIntermediateCities(ArrayList<String> ruta, String key) {
		if (rutas.containsKey(key)) {
			String[] info = rutas.get(key);
			for(int i =1;i<info.length;i++) {
				ruta.add(info[i]);
			}
		}
	}
	
	/**
	 * Permite ejecutar el algoritmo y seleccionar la ruta mas corta entre dos ciudades.
	 * @param origen. Nombre de la ciudad de origen.
	 * @param destino. Nombre de la ciudad de destino.
	 * @return String. Ruta de llegada mas corta.
	 */
	public String shorterRoute(String origen, String destino) {
		String viaje = origen+", "+destino;
		if(origen.equals(destino))
			return "Se esta dirigiendo a la misma ciudad, la ruta es 0km";
		if(rutas.containsKey(viaje)) {
			String ruta = "";
			ruta = "Ruta: "+rutas.get(viaje)[0];
			ruta += rutas.get(viaje).length>1 ? " km\n"+"Ciudades intermedias: "+intermediateCities(rutas.get(viaje)) : " km";
			return ruta;
		}else
			return "No se encontr� una ruta";
	}
	
	/**
	 * Se encarga de generar un String con cada una de las ciudades intermedias de una ruta.
	 * @param cities. Arreglo que contiene diferentes nombres de ciudades.
	 * @return String. Ciudades intermedias.
	 */
	private String intermediateCities(String[] cities) {
		String iCities = "";
		for(int i = 1;i<cities.length;i++)
			iCities += cities[i] + ", ";
		return iCities.substring(0, iCities.length()-2);
	}
	
	/**
	 * Se encarga de calcular el centro del grafo.
	 * @param pesos. Matriz con los pesos de cada arista.
	 */
	public void graphCenter(Double[][] pesos) {
		Double[] eccentricities = new Double[vertices.size()];
		for(int i=0;i<vertices.size();i++) {
			for(int j=0;j<vertices.size();j++) {
				if(eccentricities[j]==null)
					eccentricities[j]=pesos[i][j];
				else if (pesos[i][j]>eccentricities[j])
					eccentricities[j]=pesos[i][j];
			}
		}
		int min = eccentricities[0].intValue();
		graphCenter = vertices.get(0);
		for(int i=0;i<vertices.size();i++) {
			if(eccentricities[i]<min) {
				min = eccentricities[i].intValue();
				graphCenter = vertices.get(i);
			}
		}
	}
	
	/**
	 * Permite la eliminaci�n de una arista entre dos nodos (ruta entre dos ciudades).
	 * @param origen. Ciudad de origen.
	 * @param destino. Ciudad de destino.
	 * @return String. Mensaje de respuesta.
	 */
	public String breakRoute(String origen, String destino) {
		String[] ruta = null;
		String[] inverted = null;
		for(String[] a : aristas) {
			if(a[0].equals(origen) && a[1].equals(destino))
				ruta = a;
			if(a[1].equals(origen) && a[0].equals(destino))
				inverted = a;
		}
		if(ruta != null) {
			aristas.remove(ruta);
			aristas.remove(inverted);
			try {
				matrizAdyacencias();
				return "Ruta eliminada correctamente, se han recalculado las rutas mas cortas.";
			} catch (InvalidGraph e) {
				return "Ha ocurrido un error al tratar de eliminar esta ruta.";
			}
		}else
			return"No se ha encontrado la ruta especificada.";
	}
	
	/**
	 * Permite crear nodos (si las ciudades de origen o destino no existen) y establecer relaciones(rutas) entre ellos.
	 * @param origen. Nombre de la ciudad de origen.
	 * @param destino. Nombre de la ciudad de destino.
	 * @param peso. Peso (Km) de la arista.
	 * @return String. Mensaje de respuesta.
	 * @throws InvalidGraph
	 */
	public String newRoute(String origen, String destino, int peso) throws InvalidGraph {
		String[] ruta = null;
		String[] inverted = null;
		int indexA = -1;
		int indexB = -1;
		for(int i=0;i<aristas.size();i++) {
			String[] arista = aristas.get(i);
			if(arista[0].equals(origen) && arista[1].equals(destino)) {
				ruta = arista;
				indexA = i;
			}
			if(arista[1].equals(origen) && arista[0].equals(destino)) {
				inverted = arista;
				indexB = i;
			}
		}
		if(ruta != null) {
			if(Integer.parseInt(ruta[2])<peso)
				return "Ya existe una ruta entre estas ciudades, con una distancia menor.";
			else {
				aristas.get(indexA)[2] = String.valueOf(peso);
				aristas.get(indexB)[2] = String.valueOf(peso);
				try {
					matrizAdyacencias();
					return "Ya existe una ruta entre estas ciudades, se ha modificado la distancia.";
				} catch (InvalidGraph e) {
					return "Ha ocurrido un error al actualizar el grafo.";
				}
			}
		}else {
			String[] newRoute = {origen,destino,String.valueOf(peso)};
			String[] invertedNew = {destino,origen,String.valueOf(peso)};
			aristas.add(newRoute);
			aristas.add(invertedNew);
			vertices.add(origen);
			vertices.add(destino);
			try {
				matrizAdyacencias();
				return "Ruta agregada. Se han recalculado las rutas mas cortas.";
			}catch(InvalidGraph e) {
				aristas.remove(newRoute);
				aristas.remove(invertedNew);
				vertices.remove(origen);
				vertices.remove(destino);
				matrizAdyacencias();
				return "Incluir esta ruta convertiria al grafo en no convexo, se ha omitido la accion.";
			}
		}
	}
	
	public void rewriteFile() throws IOException {
		ArrayList<String> parejas = new ArrayList<String>();
		for(String[] a : aristas) {
			String viaje = a[0] + ", "+a[1];
			String invertedViaje = a[1]+", "+a[0];
			if(!parejas.contains(viaje) && !parejas.contains(invertedViaje))
				FileController.writeFile(a[0] + " " + a[1] + " "+a[2]+"\n");
			parejas.add(viaje);
			parejas.add(invertedViaje);
		}
	}
	
	/**
	 * Metodo getter del centro del grafo.
	 * @return String. Centro del grafo.
	 */
	public String getGraphCenter() {
		return this.graphCenter;
	}
}
