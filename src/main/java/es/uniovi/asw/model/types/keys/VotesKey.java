package es.uniovi.asw.model.types.keys;

import java.io.Serializable;

public class VotesKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long citizen;
	private Long proposal;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((citizen == null) ? 0 : citizen.hashCode());
		result = prime * result + ((proposal == null) ? 0 : proposal.hashCode());
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
		VotesKey other = (VotesKey) obj;
		if (citizen == null) {
			if (other.citizen != null)
				return false;
		} else if (!citizen.equals(other.citizen))
			return false;
		if (proposal == null) {
			if (other.proposal != null)
				return false;
		} else if (!proposal.equals(other.proposal))
			return false;
		return true;
	}
	
	

}
