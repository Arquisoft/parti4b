package es.uniovi.asw.persistence.impl;

import java.util.List;

import es.uniovi.asw.business.parser.Insert;
import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;

/**
 * Created by igm1990 on 15/03/2017.
 */
public class InsertP implements Insert {

	@Override
	public void save(Factories factories, List<Citizen> citizens) throws CitizenException {
		factories.getPersistenceFactory().newCitizenRepository().save(citizens);
	}
}
