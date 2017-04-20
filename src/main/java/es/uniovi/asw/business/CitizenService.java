package es.uniovi.asw.business;

import es.uniovi.asw.model.Citizen;

import java.util.List;

public interface CitizenService {

	public List<Citizen> findAll();

	public Citizen findById(long id);

	public Citizen findByDni(String dni);

	public void save(Citizen citizen);

	public Citizen findLoggableUser(String dni, String password);

	public int count();

	public void delete(Citizen citizen);
}
