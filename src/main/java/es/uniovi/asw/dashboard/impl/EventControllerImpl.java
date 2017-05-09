package es.uniovi.asw.dashboard.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;

import es.uniovi.asw.dashboard.EventController;
import es.uniovi.asw.model.types.topics.Topics;

public class EventControllerImpl implements EventController {

	public static Map<String, Integer> votedProposals = new HashMap<String, Integer>();
	
	@KafkaListener(topics = Topics.POSITIVE_VOTE)	
	@Override
	public void positiveVote(String data) {
		Integer aux = 0;
		if(votedProposals.containsKey(data)){
			aux = votedProposals.get(data);
			aux++;
			votedProposals.replace(data, aux);
		} else
			votedProposals.put(data,1);
	}

	@KafkaListener(topics = Topics.NEGATIVE_VOTE)
	@Override
	public void negativeVote(String data) {
		Integer aux = 0;
		if(votedProposals.containsKey(data)){
			aux = votedProposals.get(data);
			aux--;
			votedProposals.replace(data, aux);
		} else
			votedProposals.put(data,1);
	}

}
