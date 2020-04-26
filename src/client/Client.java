package client;

import java.io.IOException;

import utilities.Connection;
import utilities.ConsoleHelper;
import utilities.Message;
import utilities.MessageType;
public class Client {
	protected Connection connection ;
	private  volatile boolean clientConnected = false  ;
	
	public static void main(String[] args) {
		new Client().run();
	}
	
	public void run() {
		SocketThread  socketThread = getSocketThread();
		socketThread.setDaemon(true);
		socketThread.start();
		synchronized(this) {
		  try {
		while(!clientConnected) wait();
		  } catch(Exception e) {
			  ConsoleHelper.writeMessage("An error is occured ");
			  return ;
		  }
		}
		if(clientConnected) ConsoleHelper.writeMessage("Connection established. To exit, enter 'exit'.");
		else ConsoleHelper.writeMessage( "An error occurred while working with the client.");
		String text ="";
		while( clientConnected && !text.equals("exit")) {
			text = ConsoleHelper.readString();
			if(shouldSendTextFromConsole()) sendTextMessage(text);
		}
		
	}
	protected String getServerAddress() {
		ConsoleHelper.writeMessage("Enter the server address ");
		return ConsoleHelper.readString();
	}
	protected int getServerPort() {
		ConsoleHelper.writeMessage("Enter the server port ");
		return ConsoleHelper.readInt();
	}
	protected String getUserName() {
		ConsoleHelper.writeMessage("Enter your username ");
		return ConsoleHelper.readString();	
	}
	protected boolean shouldSendTextFromConsole() {
		return true;
	}
	protected SocketThread getSocketThread() {
		return new SocketThread();
	}
	protected void sendTextMessage(String text) {
		try {
			Message textMessage = new Message(MessageType.TEXT, text);		
			connection.send(textMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			clientConnected = false;
			e.printStackTrace();
		}
	}
	public class SocketThread  extends Thread {
		public void run() {
			
		}
		
	}
}
