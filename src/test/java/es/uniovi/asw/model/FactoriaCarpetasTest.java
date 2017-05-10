package es.uniovi.asw.model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Random;

import org.junit.Test;

import es.uniovi.asw.util.FactoryCarpetas;

public class FactoriaCarpetasTest {
	
	@Test
	public void crearCarpeta() {
		new FactoryCarpetas().crearCarpeta("ivan");
		File f = new File("ivan");
		
		assertTrue(f.exists());
		
		f.delete();
		
	}
}