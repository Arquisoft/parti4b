package es.uniovi.asw.dbManagement.impl;

import java.util.List;

import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.participationSystem.parser.Insert;

/**
 * Created by igm1990 on 15/03/2017.
 */
public class InsertP implements Insert {

	@Override
	public void save(Factories factories, List<Citizen> citizens) throws CitizenException {
		factories.getPersistenceFactory().newCitizenRepository().save(citizens);
	}
}
