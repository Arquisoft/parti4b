package es.uniovi.asw.business.reportwritter;

import java.io.*;
import java.util.GregorianCalendar;

import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.util.FactoryCarpetas;

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
		new FactoryCarpetas().crearCarpeta("Log");
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

}