package es.uniovi.asw.util;

import java.util.List;

import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.participationSystem.reportwriter.WreportP;

/**
 * Clase encarga de mostrar por pantalla datos al usuario.
 * 
 * @author Iván González Mahagamage
 *
 */
public class Printer {

	/**
	 * Informa al usuario de la excepción ocurrida durante la ejecución del
	 * programa y guarda dicha excepcion en el fichero LOG.txt
	 * 
	 * @param e
	 *            Excepción ocurrida
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	public void printCitizenException(Exception e) throws CitizenException {
		new WreportP().grabarError(e.getLocalizedMessage());
		System.err.print(e.getLocalizedMessage());
	}

	/**
	 * Método encargado de imprimir toda la información del ciudadano.
	 * 
	 * @param citizens
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	public void imprimirCitizen(List<Citizen> citizens)
			throws CitizenException {
		for (Citizen citizen : citizens) {
			System.out.println("ID: " + citizen.getId());
			System.out.println("DNI: " + citizen.getDni());
			System.out.println("PASS: " + citizen.getPassword());
		}
	}
}
