package es.uniovi.asw.model;

import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.model.util.EncryptMD5;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TCITIZENS")
public class Citizen {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String nombre;
	@NotNull
	private String apellidos;
	@NotNull
	private String email;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;

	@NotNull
	private String residencia;

	@NotNull
	private String nacionalidad;

	@NotNull
	private String dni;

	@NotNull
	private String password;

	private boolean isAdmin;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "citizen", fetch = FetchType.EAGER)
	private Set<Commentary> comentarios = new HashSet<Commentary>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "citizen", fetch = FetchType.EAGER)
	private Set<Vote> votes = new HashSet<Vote>();

	Citizen() {
	}

	public Citizen(String nombre, String apellidos, String email,
			Date fechaNacimiento, String residencia, String nacionalidad,
			String dni) throws NoSuchAlgorithmException, CitizenException {
		setNombre(nombre);
		setApellidos(apellidos);
		setEmail(email);
		setFechaNacimiento(fechaNacimiento);
		setResidencia(residencia);
		setNacionalidad(nacionalidad);
		setDni(dni);
		setPassword(generarPassword());
		setAdmin(false);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento)
			throws CitizenException {
		Calendar calendar = GregorianCalendar.getInstance();
		try {
			if (fechaNacimiento.before(calendar.getTime()))
				this.fechaNacimiento = fechaNacimiento;
			else
				throw new CitizenException(
						"La fecha de nacimiento es posterior al dia actual.");
		} catch (NullPointerException e) {
			throw new CitizenException(
					"La fecha de nacimiento no puede ser null.");
		}
	}

	public String getResidencia() {
		return residencia;
	}

	public void setResidencia(String residencia) {
		this.residencia = residencia;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password)
			throws NoSuchAlgorithmException, CitizenException {
		this.password = new EncryptMD5().encrypting(password);
	}

	private String generarPassword()
			throws NoSuchAlgorithmException, CitizenException {
		String password = "";
		Calendar calendar = GregorianCalendar.getInstance();
		Random r = new Random(calendar.getTimeInMillis());
		int i = 0;

		while (i < 10) {
			char c = (char) r.nextInt(255);
			if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z')
					|| (c >= 'a' && c <= 'z')) {
				password += c;
				i++;
			}
		}

		return new EncryptMD5().encrypting(password);
	}

	public Set<Commentary> getComentarios() {
		return new HashSet<Commentary>(comentarios);
	}

	public Set<Commentary> _getComentarios() {
		return comentarios;
	}

	public void setComentarios(Set<Commentary> comentarios) {
		this.comentarios = comentarios;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Set<Vote> getVotes() {
		return new HashSet<Vote>(votes);
	}

	public Set<Vote> _getVotes() {
		return votes;
	}

	public void setVotes(Set<Vote> votes) {
		this.votes = votes;
	}

	@Override
	public String toString() {
		return "Citizen [id=" + id + ", nombre=" + nombre + ", apellidos="
				+ apellidos + ", email=" + email + ", fechaNacimiento="
				+ fechaNacimiento + ", residencia=" + residencia
				+ ", nacionalidad=" + nacionalidad + ", dni=" + dni
				+ ", password=" + password + ", isAdmin=" + isAdmin
				+ ", comentarios=" + comentarios.size() + ", votes="
				+ votes.size() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citizen other = (Citizen) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
