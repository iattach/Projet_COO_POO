package Model;

import java.util.ArrayList;

public class Conversation {
	
	private ArrayList<Message> conv = new ArrayList<Message>();
	private Address destination;
	
	public Conversation(Address destination) {
		this.destination = destination;
	}

	public ArrayList<Message> getConv() {
		return conv;
	}

	public void setConv(ArrayList<Message> conv) {
		this.conv = conv;
	}
	
	public void addMessage(Message msg) {
		this.conv.add(msg);
	}

	public Message getMessage(int index) {
		return this.conv.get(index);
	}
	
	public Message[] getAllMessages() {
		return this.conv.toArray(new Message[this.conv.size()]);
	}
	
	public Address getDestination() {
		return destination;
	}

	public void setDestination(Address destination) {
		this.destination = destination;
	}
	
	public boolean isEmpty() {
		return this.conv.isEmpty();
	}

}
