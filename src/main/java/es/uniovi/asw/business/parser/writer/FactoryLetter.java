package es.uniovi.asw.business.parser.writer;

import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.util.FactoryCarpetas;

/**
 * Clase encargada de crear la distintas clases que se ocupan de crear las
 * cartas para los ciudadanos.
 * 
 * @author Adrián García Lumbreras
 * @author Iván González Mahagamage
 *
 */
public class FactoryLetter {

	/**
	 * Método encargado de crear las carpetas donde se almacenarán las cartas y
	 * las cartas para los ciudadanos.
	 * 
	 * @param formato
	 *            Formato de la carta
	 * @return Una carta para un ciudadano.
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	public static Letter generate(String formato) throws CitizenException {
		FactoryCarpetas factory = new FactoryCarpetas();
		factory.crearCarpeta("Letter");
		factory.crearCarpeta("Letter/" + formato.toUpperCase());
		return crearCarta(formato.toLowerCase());
	}

	/**
	 * Método encargado de seleccionar la clase para generar la carta y generar
	 * esta.
	 * 
	 * @param formato
	 *            Formato de la carta
	 * @return Una carta para un ciudadano.
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	private static Letter crearCarta(String formato) throws CitizenException {
		if ("txt".equals(formato)) {
			return new TXTLetter();
		} else if ("pdf".equals(formato)) {
			return new PDFLetter();
		} else if ("word".equals(formato)) {
			return new WordLetter();
		} else {
			throw new CitizenException(
					"ERROR. Formato (" + formato + ") no soportado");
		}
	}
}
