package es.uniovi.asw.business.impl;

import es.uniovi.asw.business.CitizenService;
import es.uniovi.asw.model.Citizen;

import es.uniovi.asw.persistence.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CitizenServiceImpl implements CitizenService {
	@Autowired
	private CitizenRepository citizenRepository;

	@Override
	public List<Citizen> findAll() {
		return citizenRepository.findAll();
	}

	@Override
	public int count() {
		return (int) citizenRepository.count();
	}

	@Override
	public Citizen findById(long id) {
		return citizenRepository.findByID(id);
	}

	@Override
	public Citizen findByDni(String dni) {
		return citizenRepository.findByDni(dni);
	}

	@Override
	public void save(Citizen citizen) {
		citizenRepository.save(citizen);
	}

	@Override
	public Citizen findLoggableUser(String name, String password) {
		return citizenRepository.findLoggableUser(name, password);
	}
}
