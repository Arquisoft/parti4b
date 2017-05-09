package es.uniovi.asw.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import es.uniovi.asw.dbManagement.PersistenceFactory;
import es.uniovi.asw.participationSystem.ServicesFactory;

@Configuration
@EnableAutoConfiguration
public class Factories {

	@Autowired
	private ServicesFactory servicesFactory;

	@Autowired
	private PersistenceFactory persistenceFactory;

	public ServicesFactory getServicesFactory() {
		return servicesFactory;
	}

	public PersistenceFactory getPersistenceFactory() {
		return persistenceFactory;
	}

}
