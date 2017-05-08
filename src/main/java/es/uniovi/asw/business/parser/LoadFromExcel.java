package es.uniovi.asw.business.parser;

import java.io.*;
import java.util.*;

import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadFromExcel implements Parser {


	/**
	 * Leemos los parametros del excel y lo vamos almacenando en una lista de
	 * ciudadanos que serán los que metamos en la BBDD.
	 *
	 * @param fichero
	 *            fichero de tipo excel
	 * @return retorna una lista con todos los ciudadanos que se han incluido en
	 *         el fichero
	 * @throws CitizenException
	 *             Cualquier problema ocurrido durante la ejecución del método.
	 */
	public List<Citizen> loadUsers(File fichero) throws CitizenException {

		List<Citizen> citizens = new ArrayList<Citizen>();
		FileInputStream file = null;
		try {
			try {
				file = new FileInputStream(fichero);
			} catch (NullPointerException e) {
				throw new CitizenException(
						"No se puede pasar como fichero un null");
			}
		} catch (FileNotFoundException e) {
			throw new CitizenException("Fichero no encontrado");
		}

		// Obtenemos la hoja de votantes del censo
		XSSFWorkbook patron;
		try {
			patron = new XSSFWorkbook(file);
		} catch (IOException e) {
			throw new CitizenException(
					"No hemos obtenido la hoja de votantes del censo");

		}

		// Obtenemos la primera hoja del libro excel
		XSSFSheet hoja = patron.getSheetAt(0);

		// Iteramos sobre cada fila de la primera hoja
		Iterator<Row> rowIterator = hoja.iterator();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// Para cada fila, iteramos a través de cada una de sus columnas
			Iterator<Cell> columnas = row.cellIterator();
			loadDataCitizen(columnas, citizens); // Cargamos los datos del
			// votante
		}
		try {
			patron.close();
		} catch (IOException e) {
			throw new CitizenException(
					"No se ha podido cerrar la hoja del libro excel.");
		}
		return citizens;
	}

	/**
	 * Método que crea el Citizen y lo almacena en la lista
	 *
	 * @param columnas
	 *            son los datos de cada fila e iteramos por columna para obtener
	 *            cada dato
	 * @param citizens
	 *            lista donde se va a almacenar cada Citizen
	 * @throws CitizenException
	 *             Excepción ocurrida durante la ejecución
	 */
	private void loadDataCitizen(Iterator<Cell> columnas,
			List<Citizen> citizens) throws CitizenException {
		try {
			String nombre = columnas.next().getStringCellValue();
			String apellidos = columnas.next().getStringCellValue();
			String email = columnas.next().getStringCellValue();
			Date fechaNacimiento = new java.sql.Date(
					columnas.next().getDateCellValue().getTime());
			String residencia = columnas.next().getStringCellValue();
			String nacionalidad = columnas.next().getStringCellValue();
			String dni = columnas.next().getStringCellValue();
			Citizen citizen = new Citizen(nombre, apellidos, email,
					fechaNacimiento, residencia, nacionalidad, dni);
			citizens.add(citizen);
		} catch (Exception ne) {
			ne.printStackTrace();
			throw new CitizenException("Error en el archivo.");
		}
	}
}
