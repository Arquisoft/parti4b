package es.uniovi.asw.participationSystem.parser.writer;

import java.io.*;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import es.uniovi.asw.model.Citizen;

/**
 * Clase encargada de crear las cartas con formato PDF.
 * 
 * @author Adrián García Lumbreras
 * @author Iván González Mahagamage
 *
 */
public class PDFLetter extends TemplateLetter {
	private Document doc;

	@Override
	protected String indicarTipo() {
		return "PDF";
	}

	@Override
	protected void crearCarta(Citizen citizen)
			throws FileNotFoundException, DocumentException {
		doc = null;
		FileOutputStream letter = null;
		letter = new FileOutputStream(
				"Letter/PDF/" + citizen.getDni() + ".pdf");
		doc = new Document();
		PdfWriter.getInstance(doc, letter);
		doc.open();
		doc.add(new Paragraph("Usuario: " + citizen.getDni() + "\n"
				+ "Password: " + citizen.getPassword()));
	}

	@Override
	protected void cerrarCarta(Citizen citizen) {
		if (doc != null) {
			doc.close();
			System.out.println("Generada la carta en formato [PDF] para ["
					+ citizen.getDni() + "].");
		}
	}

}