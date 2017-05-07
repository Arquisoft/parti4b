package es.uniovi.asw.dashboard.impl;

import org.springframework.kafka.annotation.KafkaListener;

import es.uniovi.asw.dashboard.EventController;
import es.uniovi.asw.model.types.topics.Topics;

public class EventControllerImpl implements EventController {

	
	@KafkaListener(topics = Topics.NEW_COMMENT)	
	@Override
	public void newComment(String data) {
		
	}

	@KafkaListener(topics = Topics.POSITIVE_VOTE)	
	@Override
	public void positiveVote(String data) {

	}

	@KafkaListener(topics = Topics.NEGATIVE_VOTE)
	@Override
	public void negativeVote(String data) {

	}

}
