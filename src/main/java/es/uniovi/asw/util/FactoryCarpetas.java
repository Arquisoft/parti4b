package es.uniovi.asw.util;

import java.io.File;

/**
 * Clase encargada de crear las carpetas necesarias para nuestra aplicación.
 * 
 * @author Adrián García Lumbreras
 * @author Iván González Mahagamage
 *
 */
public class FactoryCarpetas {

	/**
	 * Método que se encarga de crear una carpeta.
	 * 
	 * @param nombreCarpeta
	 *            Nombre y ubicación de la nueva carpeta.
	 */
	public void crearCarpeta(String nombreCarpeta) {
		String nombre = "" + nombreCarpeta;
		File file = new File(nombre);
		if (!file.exists()) {
			file.mkdir();
		}
	}
}
