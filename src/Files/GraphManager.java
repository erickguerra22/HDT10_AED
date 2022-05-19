/**
 * 
 */
package Files;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author erick
 *
 */
public class GraphManager {
	private ArrayList<String[]> aristas = new ArrayList<String[]>();
	private HashMap<String, String[]> rutas=new HashMap<String, String[]>();
	private ArrayList<String> vertices = new ArrayList<String>();
	private String graphCenter = "";
	
	public void fileToGraph(String[] lines){
		for(String l : lines) {
			String[] line = l.split(" ");
			if(!aristas.contains(line))
				aristas.add(line);
			if(!vertices.contains(line[0]))
				vertices.add(line[0]);
			if(!vertices.contains(line[1]))
				vertices.add(line[1]);
		}
		matrizAdyacencias();
	}
	
	private void matrizAdyacencias(){
		Double[][] pesos = new Double[vertices.size()][vertices.size()];
		for(int i =0; i<vertices.size();i++) {
			for(int j=0;j<vertices.size();j++) {
				if(i==j)
					pesos[i][j] = 0.00;
				else {
					boolean foundAdy = false;
					for(String[] a : aristas) {
						if(a[0].equals(vertices.get(i))&&a[1].equals(vertices.get(j))) {
							pesos[i][j] = Double.parseDouble(a[2]);
							foundAdy = true;
						}
					}
					if(!foundAdy)
						pesos[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}
		floyd(pesos);
	}
	
	private void floyd(Double[][] pesos){
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
	}
	
	private void getIntermediateCities(ArrayList<String> ruta, String key) {
		if (rutas.containsKey(key)) {
			String[] info = rutas.get(key);
			for(int i =1;i<info.length;i++) {
				ruta.add(info[i]);
			}
		}
	}
	
}
