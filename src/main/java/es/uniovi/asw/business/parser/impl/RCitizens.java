package es.uniovi.asw.business.parser.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import es.uniovi.asw.business.parser.LoadFromExcel;
import es.uniovi.asw.business.parser.Parser;
import es.uniovi.asw.business.parser.ReadCitizens;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;

/**
 * Clase encargada de leer los ficheros Excel con los datos de los usuarios.
 * 
 * @author Jorge Rodríguez Fernández
 * @author Iván González Mahagamage
 *
 */
public class RCitizens implements ReadCitizens {

	@Override
	public List<Citizen> readCitizens(File fichero) throws CitizenException {
		String[] analizador;
		Parser leer = null;
		List<Citizen> citizens = new ArrayList<Citizen>();
		try {
			analizador = fichero.getName().split(Pattern.quote("."));
			if (analizador[analizador.length - 1].equals("xlsx")) {
				leer = new LoadFromExcel();
			} else {
				throw new CitizenException(
						"Error en el fichero la extensión del archivo");
			}
			citizens = leer.loadUsers(fichero);

		} catch (ArrayIndexOutOfBoundsException e) {
			throw new CitizenException("No se ha especificado la ruta de "
					+ "acceso al archivo correctamente.");
		}
		return citizens;
	}
}
