package es.uniovi.asw.model.util;

import java.math.BigInteger;
import java.security.*;

import es.uniovi.asw.model.exception.CitizenException;

public class EncryptMD5 {

	public String encrypting(String password)
			throws NoSuchAlgorithmException, CitizenException {
		String newPassword = null;
		try {
			MessageDigest msg = MessageDigest.getInstance("MD5");
			byte[] bytes = msg.digest(password.getBytes());

			BigInteger num = new BigInteger(1, bytes);
			newPassword = num.toString(16);

			while (newPassword.length() < 32) {
				newPassword = "0" + newPassword;
			}
		} catch (NullPointerException e) {
			throw new CitizenException(
					"No se puede encriptar una contraseña nula");
		}

		return newPassword;
	}
}
