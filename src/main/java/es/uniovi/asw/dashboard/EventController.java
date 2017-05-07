package es.uniovi.asw.dashboard;

public interface EventController {
	
	public void newComment(String data);
	public void positiveVote(String data);
	public void negativeVote(String data);
	
}
