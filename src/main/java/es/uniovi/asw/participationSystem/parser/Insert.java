package es.uniovi.asw.participationSystem.parser;

import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.exception.CitizenException;

import java.util.List;

/**
 * Created by igm1990 on 15/03/2017.
 */
public interface Insert {
	public void save(Factories factories, List<Citizen> citizens)
			throws CitizenException;
}
