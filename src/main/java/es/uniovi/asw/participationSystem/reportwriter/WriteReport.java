package es.uniovi.asw.participationSystem.reportwriter;

import es.uniovi.asw.model.exception.CitizenException;

/**
 * Interfaz que define los métodos para guardar datos en el log del sistema
 * 
 * @author Iván González Mahagamage
 *
 */
public interface WriteReport {
	/**
	 * Método que modifica el archivo LOG.txt para añadir una nueva excepción.
	 * 
	 * @param error
	 *            Mensaje de error devuelto por la excepción.
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	public void grabarError(String error) throws CitizenException;
}