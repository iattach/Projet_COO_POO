package Model;

import java.util.ArrayList;

public class Conversation {
	
	private ArrayList<Message> conv = new ArrayList<Message>();
	private Address destination;
	
	public Conversation(Address destination) {
		this.destination = destination;
	}

	protected ArrayList<Message> getConv() {
		return conv;
	}

	protected void setConv(ArrayList<Message> conv) {
		this.conv = conv;
	}
	
	protected void addMessage(Message msg) {
		this.conv.add(msg);
	}

	protected Message getMessage(int index) {
		return this.conv.get(index);
	}
	
	protected Message[] getAllMessages() {
		return this.conv.toArray(new Message[this.conv.size()]);
	}
	
	protected Address getDestination() {
		return destination;
	}

	protected void setDestination(Address destination) {
		this.destination = destination;
	}
	
	protected boolean isEmpty() {
		return this.conv.isEmpty();
	}

}
