package es.uniovi.asw.participationSystem.reportwriter;

import java.io.*;
import java.util.GregorianCalendar;

import es.uniovi.asw.model.exception.CitizenException;

/**
 * Clase encargar de documentar y guardar todas las excepciones ocurridas con el
 * usa del programa.
 * 
 * @author Iván González Mahagamage
 *
 */
public class WreportP implements WriteReport {

	@Override
	public void grabarError(String error) throws CitizenException {
		if ("".equals(error))
			throw new CitizenException(
					"El error a guardar en el fichero Log no puede ser vacio.");
		if (error == null)
			throw new CitizenException(
					"El error a guardar en el fichero Log no puede ser null.");
		crearCarpeta("Log");
		try {
			String mensajeLog = "(";
			mensajeLog += GregorianCalendar.getInstance().getTime() + ") -> ";
			mensajeLog += error + "\n";
			BufferedWriter fichero = new BufferedWriter(
					new FileWriter("Log/LOG.txt", true));
			fichero.write(mensajeLog);
			fichero.close();
		} catch (IOException ioe) {
			throw new CitizenException(ioe.getLocalizedMessage());
		}

	}

	public void crearCarpeta(String nombreCarpeta) {
		String nombre = "" + nombreCarpeta;
		File file = new File(nombre);
		if (!file.exists()) {
			file.mkdir();
		}
	}

}