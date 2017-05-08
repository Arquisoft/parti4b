package es.uniovi.asw.business.parser.writer;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.itextpdf.text.DocumentException;

import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;

/**
 * Clase abstracta que implementa las partes comunes a todas las clases que
 * generar cartas.
 * 
 * @author Adrián García Lumbreras
 * @author Iván González Mahagamage
 *
 */
public abstract class TemplateLetter implements Letter {
	protected String tipo;

	@Override
	public void generateLetter(Citizen citizen) throws CitizenException {
		tipo = indicarTipo();
		comprobarUsuario(citizen);
		try {
			FactoryLetter.generate(tipo);
			crearCarta(citizen);
		} catch (DocumentException e) {
			throw new CitizenException(
					"[DocumentException] No se ha podido generar "
							+ "la carta en [" + tipo + "] para el usuario "
							+ citizen.getDni());
		} catch (IOException e) {
			throw new CitizenException(
					"[IOException] No se ha podido generar " + "la carta en ["
							+ tipo + "] para el usuario " + citizen.getDni());
		} finally {
			try {
				cerrarCarta(citizen);
			} catch (IOException e2) {
				throw new CitizenException("[ERROR] [" + tipo + "] "
						+ citizen.getDni() + ": " + e2.getMessage());
			}
		}
	}

	/**
	 * Método que comprueba que toda la información obtenida del ciudadano sea
	 * válida.
	 * 
	 * @param citizen
	 *            Ciudadano del cual comprobamos sus datos.
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	private void comprobarUsuario(Citizen citizen) throws CitizenException {
		String mensaje = "El siguiente campo del usuario esta vacío -> ";
		try {
			if (citizen == null) {
				throw new CitizenException(
						"Se ha pasado un null como parámetro.");
			}
			if (citizen.getApellidos().isEmpty()) {
				throw new CitizenException(mensaje + "Apellidos");
			} else if (citizen.getDni().isEmpty()) {
				throw new CitizenException(mensaje + "DNI");
			} else if (citizen.getEmail().isEmpty()) {
				throw new CitizenException(mensaje + "Email");
			} else if (citizen.getNacionalidad().isEmpty()) {
				throw new CitizenException(mensaje + "Nacionalidad");
			} else if (citizen.getNombre().isEmpty()) {
				throw new CitizenException(mensaje + "Nombre");
			} else if (citizen.getResidencia().isEmpty())
				throw new CitizenException(mensaje + "Residencia");
		} catch (NullPointerException e) {
			throw new CitizenException(
					"No todos los campos estan inicializados");
		}
	}

	/**
	 * Método que indica el tipo de la carta.
	 * 
	 * @return El tipo de la carta.
	 */
	protected abstract String indicarTipo();

	/**
	 * Método que crear la carta dependiendo del tipo que sea.
	 * 
	 * @param citizen
	 *            Ciudadano al cual pertenece la carta.
	 * @throws FileNotFoundException
	 *             Excepción ocurrida durante la búsqueda. de la carta.
	 * @throws DocumentException
	 *             Excepción ocurrida durante la modificación de la carta.
	 * @throws IOException
	 *             Excepción ocurrida durante la modificación datos de la carta.
	 */
	protected abstract void crearCarta(Citizen citizen)
			throws FileNotFoundException, DocumentException, IOException;

	/**
	 * Método que cierra la carta dependiendo del tipo que sea.
	 * 
	 * @param citizen
	 *            Ciudadano al cual pertenece la carta.
	 * @throws IOException
	 *             Excepción ocurrida durante la modificación de la carta.
	 */
	protected abstract void cerrarCarta(Citizen citizen) throws IOException;

}
