package es.uniovi.asw.participationSystem.parser;

import java.io.File;
import java.util.List;

import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;

/**
 * Interfaz encargada de definis los métodos que leen ficheros con los datos de
 * los usuarios.
 * 
 * @author Jorge Rodríguez Fernández
 * @author Iván González Mahagamage
 *
 */
public interface ReadList {
	/**
	 * Método encargado de leer los datos de los ciudadanos.
	 * 
	 * @param fichero
	 *            Documento con datos.
	 * @return Una lista con los datos de los usuarios leidos.
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	List<Citizen> readCitizens(File fichero) throws CitizenException;
}
