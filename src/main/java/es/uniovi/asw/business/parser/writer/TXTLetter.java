package es.uniovi.asw.business.parser.writer;

import java.io.*;

import com.itextpdf.text.DocumentException;
import es.uniovi.asw.model.Citizen;

/**
 * Clase encargada de crear las cartas con formato TXT.
 * 
 * @author Adrián García Lumbreras
 * @author Iván González Mahagamage
 *
 */
public class TXTLetter extends TemplateLetter {
	private Writer writer;

	@Override
	protected String indicarTipo() {
		return "TXT";
	}

	@Override
	protected void crearCarta(Citizen citizen)
			throws DocumentException, IOException {
		File letter = new File("Letter/TXT/" + citizen.getDni() + ".txt");
		writer = new FileWriter(letter);
		writer.write("Usuario: " + citizen.getDni() + "\n" + "Password: "
				+ citizen.getPassword());
	}

	@Override
	protected void cerrarCarta(Citizen citizen) throws IOException {
		if (writer != null) {
			writer.close();
			System.out.println("Generada la carta en formato [TXT] para ["
					+ citizen.getDni() + "].");
		}
	}
}
