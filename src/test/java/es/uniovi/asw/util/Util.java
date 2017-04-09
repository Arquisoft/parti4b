package es.uniovi.asw.util;

import java.util.Random;

public class Util {
	public static int generarAleatorio(int maximo) {
		Random random = new Random();
		return (int) (random.nextDouble() * maximo) + 1;
	}
}
