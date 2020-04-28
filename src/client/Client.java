package client;

import java.io.IOException;
import java.net.Socket;

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
		if(clientConnected) {
			
		ConsoleHelper.writeMessage("Connection established. To exit, enter 'exit'.");		
		String text ="";
		while( clientConnected ) {
			text = ConsoleHelper.readString();
			if(text.equals("exit")) break;
			
			if(shouldSendTextFromConsole()) sendTextMessage(text);
		  }
		}
		else ConsoleHelper.writeMessage( "An error occurred while working with the client.");
		
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
			try {
			String serverAddress = getServerAddress();
			int port = getServerPort();
			Socket socket = new Socket(serverAddress, port);
			connection = new Connection(socket);
			clientHandshake();
			clientMainLoop();
			}
			catch(IOException | ClassNotFoundException ex) {
				notifyConnectionStatusChanged(false);
			}
			
		}
		protected void clientHandshake() throws IOException, ClassNotFoundException{
			while(!isInterrupted()) {
				Message message = 	connection.receive();
				if(message.getType() == MessageType.NAME_REQUEST) {			
					Message response = new Message(MessageType.USER_NAME,getUserName());
					connection.send(response);
				}
				else if(message.getType() == MessageType.NAME_ACCEPTED) {
					notifyConnectionStatusChanged(true);
					return ;
				}
				else 	throw new IOException("Unexpected MessageType");					
			}
		}
		protected void clientMainLoop() throws IOException, ClassNotFoundException{
			while(!isInterrupted()) {
				Message message = 	connection.receive();
				if(message.getType() == MessageType.TEXT) {			
					processIncomingMessage(message.getData());
				}
				else if(message.getType() == MessageType.USER_ADDED) {
					informAboutAddingNewUser(message.getData());
				}
				else if(message.getType() == MessageType.USER_REMOVED) {
					informAboutDeletingNewUser(message.getData());
				}
				else 	throw new IOException("Unexpected MessageType");					
			}
		}
	
		protected void processIncomingMessage(String message) {
			ConsoleHelper.writeMessage(message);
		}
		protected void informAboutAddingNewUser(String userName) {
			ConsoleHelper.writeMessage(String.format("New user added : %s", userName));
		}
		protected void informAboutDeletingNewUser(String userName) {
			ConsoleHelper.writeMessage(String.format(" User deleted : %s", userName));
		}
		protected void notifyConnectionStatusChanged(boolean clientConnected) {
			synchronized(Client.this) {
				Client.this.clientConnected = clientConnected;
				Client.this.notify();
			}
		}
	
	}
}
