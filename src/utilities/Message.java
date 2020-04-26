package utilities;


import java.io.Serializable;

public class Message implements Serializable{
	private final MessageType type ;
	private final String data ;
    public MessageType getType() {
		return type;
	}
	public String getData() {
		return data;
	}
	public Message(MessageType type) {
		this.data = null;
		this.type = type;
	}
	public Message(MessageType type, String data) {
		this.data = data ;
		this.type = type;
	}
	

}
