package es.uniovi.asw.business.parser.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import es.uniovi.asw.model.Citizen;
import org.apache.poi.xwpf.usermodel.*;

import com.itextpdf.text.DocumentException;

/**
 * Clase encargada de crear las cartas con formato WORD.
 * 
 * @author Adrián García Lumbreras
 * @author Iván González Mahagamage
 *
 */
public class WordLetter extends TemplateLetter {
	private FileOutputStream letter;

	@Override
	protected String indicarTipo() {
		return "WORD";
	}

	@Override
	protected void crearCarta(Citizen citizen)
			throws FileNotFoundException, DocumentException, IOException {
		XWPFDocument documento = new XWPFDocument();
		File folder = new File("Letter/WORD");
		folder.mkdir();
		letter = new FileOutputStream(
				"Letter/WORD/" + citizen.getDni() + ".docx");
		XWPFParagraph paragraph = documento.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText("Usuario: " + citizen.getDni());
		run.addBreak();
		run.addBreak();
		run.setText("Password: " + citizen.getPassword());
		documento.write(letter);
		documento.close();
	}

	@Override
	protected void cerrarCarta(Citizen citizen) throws IOException {
		if (letter != null) {
			letter.close();
			System.out.println("Generada la carta en formato [WORD] para ["
					+ citizen.getDni() + "].");
		}
	}

}